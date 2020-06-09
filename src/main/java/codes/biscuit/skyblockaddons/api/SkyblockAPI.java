package codes.biscuit.skyblockaddons.api;

import codes.biscuit.skyblockaddons.api.models.SkyblockProfile;
import codes.biscuit.skyblockaddons.api.models.SmallProfile;
import codes.biscuit.skyblockaddons.utils.EnumUtils;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SkyblockAPI {

    private static final String PROFILE_LIST_PATH = "https://api.slothpixel.me/api/skyblock/profiles/%s";
    private static final String PROFILE_PATH = "https://api.slothpixel.me/api/skyblock/profile/%s/%s";

    @Getter
    private static final List<SkyblockProfile> profiles = new LinkedList<>();
    @Getter
    private static SkyblockProfile activeProfile;

    public static void update() throws IOException {
        final String uuid = Minecraft.getMinecraft().getSession().getPlayerID();
        FMLLog.info("Fetching profiles for %s (%s)...", Minecraft.getMinecraft().getSession().getUsername(), uuid);
        final List<SmallProfile> profiles = getProfileList(uuid);
        String activeProfileId = null;
        Optional<SmallProfile> lastSaveMax = profiles.stream().max((a, b) -> Long.compare(a.getLastSave(), b.getLastSave()));
        if(lastSaveMax.isPresent()) {
            FMLLog.info("Active profile: %s", lastSaveMax.get().getCuteName());
            activeProfileId = lastSaveMax.get().getProfileId();
        }
        for (SmallProfile profile : profiles) {
            SkyblockProfile skyblockProfile = getProfile(uuid, profile.getProfileId());
            SkyblockAPI.profiles.add(skyblockProfile);
            FMLLog.info("\nLoaded profile %s",skyblockProfile);
            if(skyblockProfile.getProfileId().equals(activeProfileId)) {
                activeProfile = skyblockProfile;
            }
            FMLLog.info("Finished loading %s: %s", profile.getCuteName(), skyblockProfile.getLocalMember().getSkillLevel(EnumUtils.SkillType.TAMING));
        }
        FMLLog.info("Active Pet: %s", getActiveProfile().getLocalMember().getActivePet());
    }

    public static List<SmallProfile> getProfileList(final String uuid) throws IOException {
        Map<String, SmallProfile> profiles = apiRequest(String.format(PROFILE_LIST_PATH, uuid), new TypeToken<Map<String, SmallProfile>>(){});
        return new LinkedList<>(profiles.values());
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
