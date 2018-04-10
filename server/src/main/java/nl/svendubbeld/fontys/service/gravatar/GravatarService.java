package nl.svendubbeld.fontys.service.gravatar;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import com.google.common.io.ByteStreams;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * Helper for serving Gravatar images.
 */
@Singleton
public class GravatarService {

    private static final Logger logger = LoggerFactory.getLogger(GravatarService.class);

    private LoadingCache<GravatarRequest, byte[]> gravatarCache = CacheBuilder.newBuilder()
            .maximumWeight(100_000_000)
            .weigher((Weigher<GravatarRequest, byte[]>) (key, value) -> value.length)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<GravatarRequest, byte[]>() {
                @Override
                public byte[] load(@NotNull GravatarRequest key) throws Exception {
                    return download(key.getEmail(), key.getSize());
                }
            });

    /**
     * Get the Gravatar image for a user. The image may be cached.
     *
     * @param request The request for the image.
     * @return The Gravatar image for a user.
     */
    public byte[] get(GravatarRequest request) {
        return gravatarCache.getUnchecked(request);
    }

    /**
     * Convert a byte array to string.
     *
     * @param bytes The bytes to convert.
     * @return The resulting string.
     * @see <a href="https://en.gravatar.com/site/implement/images/java/">https://en.gravatar.com/site/implement/images/java/</a>
     */
    private String hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }
        return stringBuilder.toString();
    }

    /**
     * Get the MD5 hash of a string.
     *
     * @param message The string to hash.
     * @return The MD5 hash of a string.
     * @see <a href="https://en.gravatar.com/site/implement/images/java/">https://en.gravatar.com/site/implement/images/java/</a>
     */
    private String getHash(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Download the Gravatar image for a user.
     *
     * @param email The email of the user.
     * @param size  The size of the resulting image.
     * @return The Gravatar image for a user.
     * @throws IOException in case of a problem or the connection was aborted.
     */
    private byte[] download(String email, int size) throws IOException {
        String hash = getHash(email);

        HttpUriRequest request = RequestBuilder.create("GET")
                .setUri("http://gravatar.com/avatar/" + hash) // TODO: HTTPS?
                .addParameter("s", String.valueOf(size))
                .addParameter("d", "retro")
                .build();

        try (
                CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse response = client.execute(request)
        ) {
            return ByteStreams.toByteArray(response.getEntity().getContent());
        } catch (IOException e) {
            logger.error("Could not download Gravatar image", e);
            throw e;
        }
    }
}
