package com.theapache64.automotion.core

import com.theapache64.automotion.utils.DateTimeUtils
import com.theapache64.automotion.utils.JarUtils
import org.apache.commons.cli.*
import java.io.File
import java.util.*

/**
 * To parse command passed to the CLI
 */
class CommandParser(
    private val args: Array<String>
) {


    companion object {
        private const val OPT_HELP = "h"
        private const val OPT_HELP_LONG = "help"

        private const val OPT_INPUT_VIDEO = "v"
        private const val OPT_INPUT_VIDEO_LONG = "video"

        private const val OPT_BGM = "bgm"
        private const val OPT_BGM_LONG = "background-music"

        private const val OPT_TITLE = "t"
        private const val OPT_TITLE_LONG = "title"
        private const val OPT_SUB_TITLE = "st"
        private const val OPT_SUB_TITLE_LONG = "sub-title"
        private const val OPT_VIDEO_LANG = "vl"
        private const val OPT_VIDEO_LANG_LONG = "video-lang"
        private const val OPT_MIN_TL_SRC_LEN = "mtl"
        private const val OPT_MIN_TL_SRC_LEN_LONG = "min-tl-src-len"
        private const val OPT_TIMELAPSE_SPEED = "tls"
        private const val OPT_TIMELAPSE_SPEED_LONG = "timelapse-speed"
        private const val OPT_INTRO_DURATION = "id"
        private const val OPT_INTRO_DURATION_LONG = "intro-duration"
        private const val OPT_CREDITS_DURATION = "cr"
        private const val OPT_CREDITS_DURATION_LONG = "credits-duration"
        private const val OPT_WATERMARK = "wm"
        private const val OPT_WATERMARK_LONG = "watermark"
        private const val OPT_INTRO_TITLE = "it"
        private const val OPT_INTRO_TITLE_LONG = "intro-title"
        private const val OPT_CREDITS_TITLE = "ct"
        private const val OPT_CREDITS_TITLE_LONG = "credits-title"
        private const val OPT_INTRO_SUB_TITLE = "ist"
        private const val OPT_INTRO_SUB_TITLE_LONG = "intro-sub-title"
        private const val OPT_CREDITS_SUB_TITLE = "cst"
        private const val OPT_CREDITS_SUB_TITLE_LONG = "credits-sub-title"
        private const val OPT_FONT = "f"
        private const val OPT_FONT_LONG = "font"
        private const val OPT_HIGHLIGHT = "hl"
        private const val OPT_HIGHLIGHT_LONG = "highlight"
        private const val OPT_WATERMARK_COLOR = "wmc"
        private const val OPT_WATERMARK_TEXT_COLOR_LONG = "wm-color"
        private const val OPT_WATERMARK_FONT_SIZE = "wms"
        private const val OPT_WATERMARK_FONT_SIZE_LONG = "wm-size"
        private const val OPT_WATERMARK_BG_COLOR = "wmbg"
        private const val OPT_WATERMARK_BG_COLOR_LONG = "wm-background-color"
        private const val OPT_WATERMARK_BG_OPACITY = "wmbgo"
        private const val OPT_WATERMARK_BG_OPACITY_LONG = "wm-background-opacity"
        private const val OPT_TITLE_FONT_SIZE = "tfs"
        private const val OPT_TITLE_FONT_SIZE_LONG = "title-font-size"
        private const val OPT_SUB_TITLE_FONT_SIZE = "stfs"
        private const val OPT_SUB_TITLE_FONT_SIZE_LONG = "sub-title-font-size"
        private const val OPT_TITLE_COLOR = "tc"
        private const val OPT_TITLE_COLOR_LONG = "title-color"
        private const val OPT_SUB_TITLE_COLOR = "stc"
        private const val OPT_SUB_TITLE_COLOR_LONG = "sub-title-color"
        private const val OPT_BG_COLOR = "bg"
        private const val OPT_BG_COLOR_LONG = "background-color"


        /**
         * Default values
         */
        private const val DEFAULT_LANGUAGE = "en"
        private const val DEFAULT_MIN_TIMESTAMP_LENGTH = 2
        const val DEFAULT_TIMELAPSE_SPEED = 0.25f
        const val DEFAULT_MIN_TIMELAPSE_SRC_LENGTH = DEFAULT_MIN_TIMESTAMP_LENGTH / DEFAULT_TIMELAPSE_SPEED
        private const val DEFAULT_INTRO_DURATION = 5
        private const val DEFAULT_CREDITS_DURATION = 5
        private val DEFAULT_FONT = "${JarUtils.getJarDir()}lab/komikax.ttf"
        private const val DEFAULT_WATERMARK_COLOR = "white"
        private const val DEFAULT_WATERMARK_FONT_SIZE = 24
        private const val DEFAULT_WATERMARK_BG_COLOR = "black"
        private const val DEFAULT_WATERMARK_BG_OPACITY = 0.5f
        private const val DEFAULT_TITLE_FONT_SIZE = 30
        private const val DEFAULT_SUB_TITLE_FONT_SIZE = 15
        private const val DEFAULT_TITLE_COLOR = "white"
        private const val DEFAULT_SUB_TITLE_COLOR = "gray"
        private const val DEFAULT_BG_COLOR = "black"
        private val currentUserName = System.getProperty("user.name");
        private val DEFAULT_WATERMARK = currentUserName
        private val DEFAULT_INTRO_TITLE = currentUserName
        private val DEFAULT_CREDITS_TITLE = currentUserName
        private const val DEFAULT_CREDITS_SUB_TITLE = "Thank You!"
        private val DEFAULT_INTRO_SUB_TITLE = DateTimeUtils.getDateFormatted(Date())
        private val DEFAULT_BGM = "${JarUtils.getJarDir()}lab/lost_in_time.mp3"


        private val options: Options = Options()
            .addOption(OPT_HELP, OPT_HELP_LONG, false, "To print help text")
            .addRequiredOption(OPT_INPUT_VIDEO, OPT_INPUT_VIDEO_LONG, true, "Video input (required)")
            .addOption(
                OPT_BGM,
                OPT_BGM_LONG,
                true,
                "Background music for timelapse (comma-separated, required)"
            )
            .addOption(OPT_TITLE, OPT_TITLE_LONG, true, "Intro title (required)")
            .addOption(OPT_SUB_TITLE, OPT_SUB_TITLE_LONG, true, "Intro subtitle (required)")
            .addOption(
                Option(
                    OPT_VIDEO_LANG,
                    OPT_VIDEO_LANG_LONG,
                    true,
                    "Video language, default $DEFAULT_LANGUAGE"
                )
            )
            .addOption(
                Option(
                    OPT_MIN_TL_SRC_LEN,
                    OPT_MIN_TL_SRC_LEN_LONG,
                    true,
                    "Minimum timelapse source length (in seconds)"
                )
            )
            .addOption(
                Option(
                    OPT_TIMELAPSE_SPEED,
                    OPT_TIMELAPSE_SPEED_LONG,
                    true,
                    "Timelapse speed (must be < 1). 0.5 = 2x speed, 0.25 = 4x"
                )
            )
            .addOption(
                Option(
                    OPT_INTRO_DURATION,
                    OPT_INTRO_DURATION_LONG,
                    true,
                    "Intro duration (in seconds)"
                )
            )
            .addOption(
                Option(
                    OPT_CREDITS_DURATION,
                    OPT_CREDITS_DURATION_LONG,
                    true,
                    "Credits duration (in seconds)"
                )
            )
            .addOption(
                OPT_WATERMARK,
                OPT_WATERMARK_LONG,
                true,
                "Watermark text. Default = logged in username"
            )
            .addOption(
                OPT_INTRO_TITLE,
                OPT_INTRO_TITLE_LONG,
                true,
                "Intro title"
            )
            .addOption(
                OPT_CREDITS_TITLE,
                OPT_CREDITS_TITLE_LONG,
                true,
                "Credits title"
            )
            .addOption(
                OPT_INTRO_SUB_TITLE,
                OPT_INTRO_SUB_TITLE_LONG,
                true,
                "Intro sub title"
            )
            .addOption(
                OPT_CREDITS_SUB_TITLE,
                OPT_CREDITS_SUB_TITLE_LONG,
                true,
                "Credits sub title"
            )
            .addOption(
                OPT_FONT,
                OPT_FONT_LONG,
                true,
                "Font file path"
            )
            .addOption(
                OPT_HIGHLIGHT,
                OPT_HIGHLIGHT_LONG,
                true,
                "Highlight of the video. HH:mm:ss-ss format (from- to seconds)"
            )
            .addOption(
                OPT_WATERMARK_COLOR,
                OPT_WATERMARK_TEXT_COLOR_LONG,
                true,
                "Watermark text color. Default $DEFAULT_WATERMARK_COLOR"
            )
            .addOption(
                OPT_WATERMARK_FONT_SIZE,
                OPT_WATERMARK_FONT_SIZE_LONG,
                true,
                "Watermark text size. Default $DEFAULT_WATERMARK_FONT_SIZE"
            )
            .addOption(
                OPT_WATERMARK_BG_COLOR,
                OPT_WATERMARK_BG_COLOR_LONG,
                true,
                "Watermark background color. Default $DEFAULT_WATERMARK_BG_COLOR"
            )
            .addOption(
                OPT_WATERMARK_BG_OPACITY,
                OPT_WATERMARK_BG_OPACITY_LONG,
                true,
                "Watermark background opacity. Default $DEFAULT_WATERMARK_BG_OPACITY"
            )
            .addOption(
                OPT_TITLE_FONT_SIZE,
                OPT_TITLE_FONT_SIZE_LONG,
                true,
                "Title font size. Default $DEFAULT_TITLE_FONT_SIZE"
            )
            .addOption(
                OPT_SUB_TITLE_FONT_SIZE,
                OPT_SUB_TITLE_FONT_SIZE_LONG,
                true,
                "Sub-title font size. Default $DEFAULT_SUB_TITLE_FONT_SIZE"
            )
            .addOption(
                OPT_TITLE_COLOR,
                OPT_TITLE_COLOR_LONG,
                true,
                "Title color. Default $DEFAULT_TITLE_COLOR"
            )
            .addOption(
                OPT_SUB_TITLE_COLOR,
                OPT_SUB_TITLE_COLOR_LONG,
                true,
                "Sub title color. Default $DEFAULT_SUB_TITLE_COLOR"
            )
            .addOption(
                OPT_BG_COLOR,
                OPT_BG_COLOR_LONG,
                true,
                "Background color. Default $DEFAULT_BG_COLOR"
            )
    }

    private val cli by lazy { DefaultParser().parse(options, args) }

    fun isPrintHelp(): Boolean {
        return args.isEmpty() || args.contains("-$OPT_HELP") || args.contains("--$OPT_HELP_LONG")
    }

    /**
     * To print help text
     */
    fun printHelp() {
        HelpFormatter()
            .apply {
                optionComparator = null
            }
            .printHelp(
                "auto-motion -v input.mp4 -bgm 'bgm1.mp3, bgm2.mp3' -t 'theapache64' ",
                "A tool to edit your lengthy screen records, automatically. ",
                options,
                "enjoy! - theapache64",
                true
            )
    }

    fun getInputVideo(): File {
        return File(cli.getOptionValue(OPT_INPUT_VIDEO))
    }

    fun getBgms(): List<File> {
        val fileList = mutableListOf<File>()
        val bgms = if (cli.hasOption(OPT_BGM)) {
            cli.getOptionValues(OPT_BGM)
        } else {
            arrayOf(DEFAULT_BGM)
        }

        bgms.forEach { bgm ->
            fileList.add(File(bgm))
        }
        require(fileList.isNotEmpty()) { "Background music must be passed" }
        return fileList
    }

    fun getMinTimelapseSourceLength(): Double {
        return cli.getOptionValue(
            OPT_MIN_TL_SRC_LEN,
            DEFAULT_MIN_TIMELAPSE_SRC_LENGTH.toString()
        ).toDouble()
    }

    fun getTimelapseSpeed(): Float {
        val speed = cli.getOptionValue(
            OPT_TIMELAPSE_SPEED,
            DEFAULT_TIMELAPSE_SPEED.toString()
        ).toFloat()
        require(speed < 1) { "Timelapse speed must be less than 1" }
        return speed
    }

    fun getVideoLanguage(): String {
        return cli.getOptionValue(
            OPT_VIDEO_LANG,
            DEFAULT_LANGUAGE
        )
    }

    fun getIntroDuration(): Double {
        return cli.getOptionValue(
            OPT_INTRO_DURATION,
            DEFAULT_INTRO_DURATION.toString()
        ).toDouble()
    }

    fun getCreditsDuration(): Double {
        return cli.getOptionValue(
            OPT_CREDITS_DURATION,
            DEFAULT_CREDITS_DURATION.toString()
        ).toDouble()
    }

    fun getWatermark(): String {
        return cli.getOptionValue(OPT_WATERMARK, DEFAULT_WATERMARK)
    }

    fun getIntroTitle(): String {
        return cli.getOptionValue(
            OPT_INTRO_TITLE,
            DEFAULT_INTRO_TITLE
        )
    }

    fun getCreditsTitle(): String {
        return cli.getOptionValue(
            OPT_CREDITS_TITLE,
            DEFAULT_CREDITS_TITLE
        )
    }

    fun getIntroSubTitle(): String {
        return cli.getOptionValue(
            OPT_INTRO_SUB_TITLE,
            DEFAULT_INTRO_SUB_TITLE
        )
    }

    fun getCreditsSubTitle(): String {
        return cli.getOptionValue(
            OPT_CREDITS_SUB_TITLE,
            DEFAULT_CREDITS_SUB_TITLE
        )
    }

    fun getFontFile(): File {
        val fontPath = cli.getOptionValue(
            OPT_FONT,
            DEFAULT_FONT
        )
        return File(fontPath)
    }

    fun getHighlightSection(): Pair<Double, Double>? {
        return if (cli.hasOption(OPT_HIGHLIGHT)) {
            val hs = cli.getOptionValue(OPT_HIGHLIGHT)
            HighlightParser.parse(hs)
        } else {
            null
        }
    }

    fun getWatermarkTextColor(): String {
        return cli.getOptionValue(OPT_WATERMARK_COLOR, DEFAULT_WATERMARK_COLOR)
    }

    fun getWatermarkFontSize(): Int {
        return cli.getOptionValue(
            OPT_WATERMARK_FONT_SIZE,
            DEFAULT_WATERMARK_FONT_SIZE.toString()
        ).toInt()
    }

    fun getWatermarkBgColor(): String {
        return cli.getOptionValue(
            OPT_WATERMARK_BG_COLOR,
            DEFAULT_WATERMARK_BG_COLOR
        )
    }

    fun getWatermarkBgOpacity(): Float {
        return cli.getOptionValue(
            OPT_WATERMARK_BG_OPACITY,
            DEFAULT_WATERMARK_BG_OPACITY.toString()
        ).toFloat()
    }

    fun getTitleFontSize(): Int {
        return cli.getOptionValue(
            OPT_TITLE_FONT_SIZE,
            DEFAULT_TITLE_FONT_SIZE.toString()
        ).toInt()
    }

    fun getSubTitleFontSize(): Int {
        return cli.getOptionValue(
            OPT_SUB_TITLE_FONT_SIZE,
            DEFAULT_SUB_TITLE_FONT_SIZE.toString()
        ).toInt()
    }

    fun getTitleColor(): String {
        return cli.getOptionValue(
            OPT_TITLE_COLOR,
            DEFAULT_TITLE_COLOR
        )
    }

    fun getSubTitleColor(): String {
        return cli.getOptionValue(
            OPT_SUB_TITLE_COLOR,
            DEFAULT_SUB_TITLE_COLOR
        )
    }

    fun getBgColor(): String {
        return cli.getOptionValue(
            OPT_BG_COLOR,
            DEFAULT_BG_COLOR
        )
    }
}