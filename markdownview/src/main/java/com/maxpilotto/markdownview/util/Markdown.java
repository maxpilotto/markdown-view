package com.maxpilotto.markdownview.util;

import androidx.annotation.RawRes;

import java.io.File;

/**
 * This class is used to fetch the markdown and the stylesheet from different locations
 * <p>
 * Created on 17/06/2019 at 16:49
 *
 * @author Max Pilotto (github.com/maxpilotto, maxpilotto.com)
 */
public class Markdown {
    public enum ResourceType {
        STRING,
        RAW,
        URL,
        ASSET,
        FILE;
    }

    public static class Builder {
        private Markdown markdown;

        public Builder() {
            this.markdown = new Markdown();
        }

        public Builder withMarkdownAsset(String name) {
            markdown.markdown = name;
            markdown.markdownType = ResourceType.ASSET;

            return this;
        }

        public Builder withMarkdownRaw(@RawRes Integer id) {
            markdown.markdown = id;
            markdown.markdownType = ResourceType.RAW;

            return this;
        }

        public Builder withMarkdownFile(File file) {
            markdown.markdown = file;
            markdown.markdownType = ResourceType.FILE;

            return this;

        }

        public Builder withMarkdownUrl(String url) {
            markdown.markdown = url;
            markdown.markdownType = ResourceType.URL;

            return this;
        }

        public Builder withMarkdown(String content) {
            markdown.markdown = content;
            markdown.markdownType = ResourceType.STRING;

            return this;
        }

        public Builder withStylesheet(String stylesheet) {
            markdown.stylesheet = stylesheet;
            markdown.stylesheetType = ResourceType.STRING;

            return this;
        }

        public Builder withStylesheetFile(File file) {
            markdown.stylesheet = file;
            markdown.stylesheetType = ResourceType.FILE;

            return this;
        }

        public Builder withStylesheetRaw(@RawRes Integer id) {
            markdown.stylesheet = id;
            markdown.stylesheetType = ResourceType.RAW;

            return this;
        }

        public Builder withStylesheetAsset(String name) {
            markdown.stylesheet = name;
            markdown.stylesheetType = ResourceType.ASSET;

            return this;
        }

        public Builder withStylesheetUrl(String url) {
            markdown.stylesheet = url;
            markdown.stylesheetType = ResourceType.URL;

            return this;
        }

        public Markdown build() {
            return markdown;
        }
    }

    private Object markdown;
    private ResourceType markdownType;
    private Object stylesheet;
    private ResourceType stylesheetType;

    private Markdown() {

    }

    public boolean hasUrls() {
        return markdownType == ResourceType.URL || stylesheetType == ResourceType.URL;
    }

    public boolean hasBothUrls() {
        return markdownType == ResourceType.URL && stylesheetType == ResourceType.URL;
    }

    public Object getMarkdown() {
        return markdown;
    }

    public ResourceType getMarkdownType() {
        return markdownType;
    }

    public Object getStylesheet() {
        return stylesheet;
    }

    public ResourceType getStylesheetType() {
        return stylesheetType;
    }
}
