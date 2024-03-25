package io.gitee.felixzc.novel.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.gitee.felixzc.novel.core.constant.CacheConsts;
import io.gitee.felixzc.novel.core.constant.DatabaseConsts;
import io.gitee.felixzc.novel.dao.entity.BookChapter;
import io.gitee.felixzc.novel.dao.entity.BookInfo;
import io.gitee.felixzc.novel.dao.mapper.BookChapterMapper;
import io.gitee.felixzc.novel.dao.mapper.BookInfoMapper;
import io.gitee.felixzc.novel.dto.resp.BookInfoRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 小说信息 缓存管理类
 */
@Component
@RequiredArgsConstructor
public class BookInfoCacheManager {

    private final BookInfoMapper bookInfoMapper;

    private final BookChapterMapper bookChapterMapper;

    /**
     * 从缓存中查询小说信息（先判断缓存中是否已存在，存在则直接从缓存中取，否则执行方法体中的逻辑后缓存结果）
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public BookInfoRespDto getBookInfo(Long id) {
        return cachePutBookInfo(id);
    }

    /**
     * 缓存小说信息（不管缓存中是否存在都执行方法体中的逻辑，然后缓存起来）
     */
    @CachePut(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public BookInfoRespDto cachePutBookInfo(Long id) {
        // 查询基础信息
        BookInfo bookInfo = bookInfoMapper.selectById(id);
        // 查询首章ID
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, id)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        BookChapter firstBookChapter = bookChapterMapper.selectOne(queryWrapper);
        // 组装响应对象
        return BookInfoRespDto.builder()
                .id(bookInfo.getId())
                .bookName(bookInfo.getBookName())
                .bookDesc(bookInfo.getBookDesc())
                .bookStatus(bookInfo.getBookStatus())
                .authorId(bookInfo.getAuthorId())
                .authorName(bookInfo.getAuthorName())
                .categoryId(bookInfo.getCategoryId())
                .categoryName(bookInfo.getCategoryName())
                .commentCount(bookInfo.getCommentCount())
                .firstChapterId(firstBookChapter.getId())
                .lastChapterId(bookInfo.getLastChapterId())
                .picUrl(bookInfo.getPicUrl())
                .visitCount(bookInfo.getVisitCount())
                .wordCount(bookInfo.getWordCount())
                .build();
    }

    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public void evictBookInfoCache(Long bookId) {
        // 调用此方法自动清除小说信息的缓存
    }
}