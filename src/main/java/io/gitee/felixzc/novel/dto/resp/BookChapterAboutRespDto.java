package io.gitee.felixzc.novel.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 小说章节相关 响应DTO
 */
@Data
@Builder
public class BookChapterAboutRespDto {

    private BookChapterRespDto chapterInfo;

    /**
     * 章节总数
     */
    @Schema(description = "章节总数")
    private Long chapterTotal;

    /**
     * 内容概要（30字）
     */
    @Schema(description = " 内容概要（30字）")
    private String contentSummary;

}