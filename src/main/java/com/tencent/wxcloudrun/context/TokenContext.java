package com.tencent.wxcloudrun.context;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token内存存储
 * 使用ConcurrentHashMap存储登录态
 * @author zszleon
 */
@Component
public class TokenContext {

    /**
     * Token存储结构
     */
    public static class TokenInfo {
        private String openid;
        private Long userId;
        private long expireTime;

        public TokenInfo(String openid, Long userId, long expireTime) {
            this.openid = openid;
            this.userId = userId;
            this.expireTime = expireTime;
        }

        public String getOpenid() {
            return openid;
        }

        public Long getUserId() {
            return userId;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    /**
     * Token有效期：30天（毫秒）
     */
    private static final long TOKEN_EXPIRE_MS = 30L * 24 * 60 * 60 * 1000;

    /**
     * Token缓存：token -> TokenInfo（30天过期，自动清理）
     */
    private static final TimedCache<String, TokenInfo> TOKEN_CACHE = 
        CacheUtil.newTimedCache(TOKEN_EXPIRE_MS);

    /**
     * OpenID反向映射：openid -> token（用于快速查找和退出时清理）
     */
    private static final Map<String, String> OPENID_TOKEN_MAP = new ConcurrentHashMap<>();

    static {
        // 启动定时清理，每10分钟清理一次过期token
        TOKEN_CACHE.schedulePrune(10 * 60 * 1000);
    }

    /**
     * 生成Token并存储
     * @param openid 微信openid
     * @param userId 用户ID
     * @return 生成的token
     */
    public String createToken(String openid, Long userId) {
        String token = IdUtil.simpleUUID();
        long expireTime = System.currentTimeMillis() + TOKEN_EXPIRE_MS;

        TokenInfo tokenInfo = new TokenInfo(openid, userId, expireTime);

        TOKEN_CACHE.put(token, tokenInfo);
        OPENID_TOKEN_MAP.put(openid, token);

        return token;
    }

    /**
     * 验证Token
     * @param token token
     * @return TokenInfo或null
     */
    public TokenInfo validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        // TimedCache的get方法会自动处理过期，如果token已过期会返回null
        return TOKEN_CACHE.get(token, false);
    }

    /**
     * 移除Token（退出登录）
     * @param token token
     */
    public void removeToken(String token) {
        TokenInfo tokenInfo = TOKEN_CACHE.get(token);
        if (tokenInfo != null) {
            TOKEN_CACHE.remove(token);
            OPENID_TOKEN_MAP.remove(tokenInfo.getOpenid());
        }
    }

    /**
     * 通过openid移除Token
     * @param openid openid
     */
    public void removeTokenByOpenid(String openid) {
         String token = OPENID_TOKEN_MAP.remove(openid);
        if (token != null) {
            TOKEN_CACHE.remove(token);
        }
    }

}
