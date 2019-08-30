# MarkDownView
Android WebView extension that can display markdown files with css

## Getting started
In your project's `build.gradle`
```gradle
repositories {
	maven { url "https://jitpack.io" }
}
```

In your modules's `build.gradle`
```gradle 
dependencies {
    implementation 'com.github.maxpilotto:markdown-view:2.3'
}
```

## Usage

```java
MarkDownView markDown = findViewById(R.id.markdownView);

markDown.load(
	"# Hello world",
	"h1 { color:red; }"
);
```
<br>
<img src="https://github.com/maxpilotto/markdown-view/blob/master/.github/imgs/s1.png" alt="drawing" width="200"/>
<br>  

## Loading markdown 
Markdown and CSS can be both loaded from the following sources
+ String
+ URL
+ Raw folder
+ Assets folder
+ File  

Using the following methods

| Markdown & CSS | Markdown only |
| - | - |
|`load(String,String)` | `load(String)` |
|`loadUrl(String,String)` | `loadUrl(String)` |
|`loadRaw(int,int)` | `loadRaw(int)` |
|`loadAssets(String,String)` | `loadAssets(String)` |
|`load(File,File)` | `load(File)` |

## Loading from different souces
Markdown and CSS can be loaded from two different sources using the `Markdown` class  
E.g.

```java
markDown.load(new Markdown.Builder()
	.withMarkdownUrl("https://raw.githubusercontent.com/maxpilotto/markdown-view/master/README.md")
	.withStylesheet("h1 {color:red;} h2 {color:blue}")
	.build()
);
```

This will load the markdown file at the given link, this file, and the given CSS passed as a string
