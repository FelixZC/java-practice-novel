package io.gitee.felixzc.novel.manager.cache;

import io.gitee.felixzc.novel.core.constant.CacheConsts;
import io.gitee.felixzc.novel.dao.entity.UserInfo;
import io.gitee.felixzc.novel.dao.mapper.UserInfoMapper;
import io.gitee.felixzc.novel.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户信息 缓存管理类
 *
 */
@Component
@RequiredArgsConstructor
public class UserInfoCacheManager {

    private final UserInfoMapper userInfoMapper;

    /**
     * 查询用户信息，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
        value = CacheConsts.USER_INFO_CACHE_NAME)
    public UserInfoDto getUser(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return UserInfoDto.builder()
            .id(userInfo.getId())
            .status(userInfo.getStatus()).build();
    }


}
