package net.themorningcompany.survivalutil.modules.fun;

import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.themorningcompany.survivalutil.modules.Module;
import net.themorningcompany.survivalutil.util.MCUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.themorningcompany.survivalutil.SurvivalUtil.gson;

public class OogishplekModule extends Module {

    public OogishplekModule() {
        super("oogishplek", "Oogishplek Translator", ModuleType.FUN);
    }

    @SubscribeEvent
    public void onSendMessage(ClientChatEvent event) {
        String message = event.getOriginalMessage();
        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (player == null) return;

        // try calc command
        Matcher calcMatcher = Pattern.compile("(oogishplek\\([0-9+\\-/*^]+\\))").matcher(message);
        boolean matches = calcMatcher.find();
        if (matches) {
            String function = calcMatcher.group();
            String length = StringUtils.substringBetween(function, "oogishplek(", ")");

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                String url = String.format("https://oogishplek.jerlshoba.com/generate?length=%s", length);
                HttpGet httpGet = new HttpGet(url);

                ResponseHandler<String> responseHandler = response -> {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else if (status == 429) {
                        MCUtil.sendPlayerMessage(player, "Too many requests! Try again in a minute.", TextFormatting.RED);
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                    return null;
                };

                String responseBody = httpClient.execute(httpGet, responseHandler);
                System.out.println("----------------------------------------");
                System.out.println(responseBody);
                Map<String, String> responseMap = gson.fromJson(responseBody, new TypeToken<Map<String, String>>(){}.getType());
                event.setMessage(event.getMessage().replace(function, responseMap.get("text")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
