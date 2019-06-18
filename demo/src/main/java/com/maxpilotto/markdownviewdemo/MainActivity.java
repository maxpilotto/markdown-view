package com.maxpilotto.markdownviewdemo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maxpilotto.markdownview.MarkDownView;
import com.maxpilotto.markdownview.util.Markdown;

/**
 * Created on 17/06/2019 at 16:02
 *
 * @author Max Pilotto (github.com/maxpilotto, maxpilotto.com)
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MarkDownView markDown = findViewById(R.id.markdownView);

//        markDown.load(
//                "# Hello world",
//                "h1 { color:red; }"
//        );

//        markDown.loadUrl(
//                "https://raw.githubusercontent.com/github/personal-website/master/README.md",
//                "https://cdnjs.cloudflare.com/ajax/libs/github-markdown-css/3.0.1/github-markdown.css"
//        );

        markDown.load(
                new Markdown.Builder()
                        .withMarkdownUrl("https://raw.githubusercontent.com/maxpilotto/markdown-view/master/README.md")
                        .withStylesheetRaw(R.raw.style)
                        .build()
        );

//        markDown.loadRaw(R.raw.test,R.raw.style);
    }
}
