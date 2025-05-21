package sch.travellocal.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import sch.travellocal.common.exception.custom.AuthException;
import sch.travellocal.common.exception.error.ErrorCode;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenHelper {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final long REFRESH_TOKEN_TTL = 7 * 24 * 60 * 60;

    public void validateRefreshToken(String refreshToken) {

        jwtUtil.validateToken(refreshToken);
        String category = jwtUtil.getCategory(refreshToken);
        if (!"refresh".equals(category)) {
            throw new AuthException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!isExistRefreshToken(refreshToken)) {
            throw new AuthException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        if (isBlacklisted(refreshToken)) {
            throw new AuthException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public void saveRefreshToken(String refreshToken) {

        String username = jwtUtil.getUserName(refreshToken);
        String redisKey = "refresh_token:" + username;

        redisTemplate.opsForHash().put(redisKey, "refreshToken", refreshToken);
        redisTemplate.expire(redisKey, REFRESH_TOKEN_TTL, TimeUnit.SECONDS);
    }

    public boolean isExistRefreshToken(String refreshToken) {

        String username = jwtUtil.getUserName(refreshToken);
        String redisKey = "refresh_token:" + username;
        String storedToken = (String) redisTemplate.opsForHash().get(redisKey, "refreshToken");

        return storedToken != null && storedToken.equals(refreshToken);
    }

    public void deleteRefreshToken(String refreshToken) {

        String username = jwtUtil.getUserName(refreshToken);
        String redisKey = "refresh_token:" + username;

        redisTemplate.delete(redisKey);
    }

    public void addBlacklistRefreshToken(String refreshToken) {

        String redisKey = "blacklist:" + refreshToken;
        long ttl = jwtUtil.getExpiration(refreshToken) - System.currentTimeMillis();

        redisTemplate.opsForValue().set(redisKey, "true", ttl, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String refreshToken) {

        String value = redisTemplate.opsForValue().get("blacklist:" + refreshToken);
        return value != null;
    }
}
