import com.github.salomonbrys.gradle.sass.SassTask
import org.asciidoctor.gradle.jvm.slides.AsciidoctorJRevealJSTask
import org.asciidoctor.gradle.jvm.slides.RevealJSOptions

plugins {
    id("org.asciidoctor.jvm.revealjs") version "3.2.0"
    id("com.github.salomonbrys.gradle.sass") version "1.2.0"
}

repositories {
    jcenter()
    maven {
        url = uri("http://rubygems-proxy.torquebox.org/releases")
    }
}

val sassTask = tasks.named<SassTask>("sassCompile") {
    source = fileTree("src/style")
    outputDir = buildDir.resolve("style")
}

asciidoctorj {
    modules.diagram.use()
}

val asciidocTask = tasks.named<AsciidoctorJRevealJSTask>("asciidoctorRevealJs") {
    dependsOn(sassTask)
    setOutputDir(buildDir.resolve("presentation"))
    revealjsOptions {
        setCustomThemeLocation(buildDir.resolve("style/ox-theme.css"))
        setTransitionSpeed(RevealJSOptions.TransitionSpeed.MAX_VALUE)
        setHighlightJsThemeLocation(file("src/style/dracula.css"))
        setFragments(true)
        setPushToHistory(true)
        setControls(false)
    }
    clearSecondarySources()
    attributes(mutableMapOf(
            "partials" to file("src/docs/asciidoc/partials/"),
            "imagesdir" to "./images",
            "icons" to "font"
    ))
    resources {
        from(sourceDir) {
            include("images/**")
        }
    }
}

tasks.build {
    dependsOn(asciidocTask)
}
