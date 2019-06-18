package com.maxpilotto.markdownview;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.RawRes;

import com.maxpilotto.markdownview.util.HttpRequest;
import com.maxpilotto.markdownview.util.JString;
import com.maxpilotto.markdownview.util.Markdown;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * WebView extension that shows markdown files
 * <p>
 * Created on 17/06/2019 at 15:00
 *
 * @author Max Pilotto (github.com/maxpilotto, maxpilotto.com)
 */
public class MarkDownView extends WebView {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public MarkDownView(Context context) {
        super(context);
    }

    public MarkDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Loads the given markdown preset, which can be built using {@link Markdown.Builder}
     *
     * @param markdown Markdown preset
     */
    public void load(Markdown markdown) {
        if (markdown.hasBothUrls()) {
            loadUrl((String) markdown.getMarkdown(), (String) markdown.getStylesheet());
        } else {
            JString md = new JString();
            JString ss = new JString();

            try {
                md.set(read(markdown.getMarkdown(),markdown.getMarkdownType()));
                ss.set(read(markdown.getStylesheet(),markdown.getStylesheetType()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (markdown.hasUrls()) {
                if (markdown.getMarkdownType() == Markdown.ResourceType.URL) {
                    HttpRequest request = new HttpRequest((String) markdown.getMarkdown());
                    request.send(results -> {
                        md.set(results[0]);

                        load(md.str(), ss.str());
                    });
                }

                if (markdown.getStylesheetType() == Markdown.ResourceType.URL) {
                    HttpRequest request = new HttpRequest((String) markdown.getStylesheet());
                    request.send(results -> {
                        ss.set(results[0]);

                        load(md.str(), ss.str());
                    });
                }
            } else {
                load(md.str(), ss.str());
            }
        }
    }

    /**
     * Loads the given markdown file and css stylesheet
     *
     * @param markdown Markdown file
     * @param style    CSS stylesheet file
     */
    public void load(File markdown, File style) {
        try {
            load(read(markdown), read(style));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the given markdown file with no css stylesheet
     *
     * @param markdown Markdown file
     */
    public void load(File markdown) {
        load(markdown, null);
    }

    /**
     * Loads the given markdown string with the given stylesheet
     *
     * @param markdown String containing the markdown raw
     * @param style    String containing the stylesheet, the tags are not needed
     */
    public void load(String markdown, String style) {
        StringBuilder page = new StringBuilder();

        page.append("<html>");

        if (style != null) {
            page.append("<head>");

            if (!style.startsWith("<style>")) {
                page.append("<style>");
            }

            page.append(style);

            if (!style.endsWith("</style>")) {
                page.append("</style>");
            }

            page.append("</head>");
        }

        page.append("<body>");
        page.append(parse(markdown));
        page.append("<body>");
        page.append("</html>");

        loadDataWithBaseURL(
                null,
                page.toString(),
                "text/html",
                "UTF-8",
                null
        );
    }

    /**
     * Loads the given markdown string
     *
     * @param markdown String containing the markdown raw
     */
    public void load(String markdown) {
        load(markdown, null);
    }

    /**
     * Loads the given markdown at the given url with the given stylesheet's url
     *
     * @param markdownUrl Url to the markdown
     * @param styleUrl    Url to the stylesheet
     */
    public void loadUrl(String markdownUrl, String styleUrl) {
        HttpRequest request = new HttpRequest(markdownUrl, styleUrl);

        request.send(results -> {
            load(results[0], results[1]);
        });
    }

    /**
     * Loads the given markdown at the given url
     *
     * @param markdownUrl Url to the markdown
     */
    public void loadUrl(String markdownUrl) {
        HttpRequest request = new HttpRequest(markdownUrl);

        request.send(results -> {
            load(results[0], null);
        });
    }

    /**
     * Loads the markdown and stylesheet from the given paths
     *
     * @param markdown Markdown asset path
     * @param style    Stylesheet asset path
     */
    public void loadAssets(String markdown, String style) {
        AssetManager assetManager = getContext().getAssets();
        String md = null;
        String ss = null;

        try {
            md = read(assetManager.open(markdown));

            if (style != null) {
                ss = read(assetManager.open(style));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        load(md, ss);
    }

    /**
     * Loads the markdown from the given paths
     *
     * @param markdown Markdown asset path
     */
    public void loadAssets(String markdown) {
        loadAssets(markdown, null);
    }

    /**
     * Loads the markdown and the stylesheet from the given paths
     *
     * @param markdown Markdown raw id
     * @param style    Stylesheet raw id
     */
    public void loadRaw(@RawRes int markdown, @RawRes int style) {
        String md = read(getContext().getResources().openRawResource(markdown));
        String ss = style == 0 ? null : read(getContext().getResources().openRawResource(style));

        load(md, ss);
    }

    /**
     * Loads the markdown from the given paths
     *
     * @param markdown Markdown raw id
     */
    public void loadRaw(@RawRes int markdown) {
        loadRaw(markdown, 0);
    }

    /**
     * Reads the content of the given input stream
     *
     * @param stream Input stream to be read
     * @return Content of the input stream
     */
    private String read(InputStream stream) {
        if (stream != null) {
            StringBuilder content = new StringBuilder();
            Scanner scanner = new Scanner(stream);

            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
                content.append(LINE_SEPARATOR);
            }

            return content.toString();
        }

        return null;
    }

    /**
     * Parses the given Markdown string
     *
     * @param markdown Markdown string
     * @return HTML string
     */
    private String parse(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(document);
    }

    /**
     * Reads the content of the resource of the give Markdown preset <br>
     * This won't work with URLs
     *
     * @param resource Resource
     * @param type Resource type
     * @return Content of the resource or null
     */
    private String read(Object resource,Markdown.ResourceType type) throws IOException {
        switch (type){
            case STRING:
                return (String) resource;

            case RAW:
                return read(getContext().getResources().openRawResource((Integer) resource));

            case ASSET:
                return read(getContext().getAssets().open((String) resource));

            case FILE:
                return read((File) resource);
        }

        return null;
    }

    /**
     * Reads the content of the given file
     *
     * @param file File to read
     * @return Content of the file
     * @throws FileNotFoundException
     */
    private String read(File file) throws FileNotFoundException {
        return read(new FileInputStream(file));
    }
}
