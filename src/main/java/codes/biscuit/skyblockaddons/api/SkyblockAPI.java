package codes.biscuit.skyblockaddons.api;

import codes.biscuit.skyblockaddons.api.models.SkyblockProfile;
import codes.biscuit.skyblockaddons.api.models.SmallProfile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class SkyblockAPI {

    private static final String PROFILE_LIST_PATH = "https://api.slothpixel.me/api/skyblock/profiles/%s";
    private static final String PROFILE_PATH = "https://api.slothpixel.me/api/skyblock/profile/%s/%s";

    @Getter
    private static final Map<String, SkyblockProfile> profiles = new HashMap<>();
    @Getter
    private static SkyblockProfile activeProfile;

    public static void fetch() throws IOException {
        final String uuid = Minecraft.getMinecraft().getSession().getPlayerID();
        final String username = Minecraft.getMinecraft().getSession().getUsername();
        FMLLog.info("Fetching profiles for %s (%s)...", Minecraft.getMinecraft().getSession().getUsername(), uuid);
        final Map<String, SmallProfile> profileList = getProfileList(uuid);
        String activeProfileId = null;

        // Find active profile by finding the most recently updated one
        Optional<Map.Entry<String, SmallProfile>> lastSaveMax = profileList.entrySet().stream()
                .max((a, b) -> Long.compare(a.getValue().getLastSave(), b.getValue().getLastSave()));
        if(lastSaveMax.isPresent()) {
            FMLLog.info("Active profile: %s", lastSaveMax.get().getValue().getCuteName());
            activeProfileId = lastSaveMax.get().getKey();
        }

        // Load full profiles
        final Map<String, SkyblockProfile> fullProfiles = new HashMap<>();
        for (Map.Entry<String, SmallProfile> profile : profileList.entrySet()) {
            String profileId = profile.getKey();
            SkyblockProfile skyblockProfile = getProfile(username, profileId);
            skyblockProfile.setCuteName(profile.getValue().getCuteName());
            fullProfiles.put(profileId, skyblockProfile);
            FMLLog.info("Loaded profile %s", profile.getValue().getCuteName());
            if(skyblockProfile.getProfileId().equals(activeProfileId)) {
                activeProfile = skyblockProfile;
            }
        }

        SkyblockAPI.profiles.clear();
        SkyblockAPI.profiles.putAll(fullProfiles);

        FMLLog.info("Finished API update.");
        FMLLog.info(activeProfile.toString());
    }

    public static Map<String, SmallProfile> getProfileList(final String uuid) throws IOException {
        return apiRequest(String.format(PROFILE_LIST_PATH, uuid), new TypeToken<Map<String, SmallProfile>>(){});
    }

    public static SkyblockProfile getProfile(final String uuid, final String profileId) throws IOException {
        FMLLog.info("Requesting "+String.format(PROFILE_PATH, uuid, profileId));
        return apiRequest(String.format(PROFILE_PATH, uuid, profileId), SkyblockProfile.class);
    }

    private static <T> T apiRequest(final String path, final Class<T> type) throws IOException {
        URLConnection connection = getConnection(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        Gson gson = new Gson();
        T result = gson.fromJson(reader, type);
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            System.out.println(currentLine);
        }
        reader.close();
        return result;
    }

    private static <T> T apiRequest(final String path, final TypeToken<T> type) throws IOException {
        URLConnection connection = getConnection(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        Gson gson = new Gson();
        T result = gson.fromJson(reader, type.getType());
        reader.close();
        return result;
    }

    private static URLConnection getConnection(final String path) throws IOException {
        URL url = new URL(path);
        URLConnection connection = url.openConnection();
        connection.setReadTimeout(5000);
        connection.addRequestProperty("User-Agent", "SkyblockAddons");
        connection.setDoOutput(true);
        return connection;
    }

}
