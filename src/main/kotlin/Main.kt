import com.theapache64.automotion.core.*
import com.theapache64.automotion.utils.ComplexCommandExecutor
import com.theapache64.automotion.utils.DateTimeUtils
import java.io.File
import java.lang.Exception
import kotlin.system.exitProcess

object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        try {
            val cp = CommandParser(args)

            if (cp.isPrintHelp()) {
                cp.printHelp()
            } else {

                // Reading input from console
                val inputVideo = cp.getInputVideo()
                val bgmFiles = cp.getBgms()
                val minTimelapseSourceLength = cp.getMinTimelapseSourceLength()
                val timelapseSpeed = cp.getTimelapseSpeed()
                val videoLanguage = cp.getVideoLanguage()
                val introDuration = cp.getIntroDuration()
                val creditsDuration = cp.getCreditsDuration()
                val watermark = cp.getWatermark()
                val introTitle = cp.getIntroTitle()
                val introSubTitle = cp.getIntroSubTitle()
                val creditsTitle = cp.getCreditsTitle()
                val creditsSubTitle = cp.getCreditsSubTitle()
                val fontFile = cp.getFontFile()
                val highlightSection = cp.getHighlightSection()
                val watermarkTextColor = cp.getWatermarkTextColor()
                val watermarkFontSize = cp.getWatermarkFontSize()
                val watermarkBgColor = cp.getWatermarkBgColor()
                val watermarkBgOpacity = cp.getWatermarkBgOpacity()
                val titleFontSize = cp.getTitleFontSize()
                val subTitleFontSize = cp.getSubTitleFontSize()
                val titleColor = cp.getTitleColor()
                val subTitleColor = cp.getSubTitleColor()
                val bgColor = cp.getBgColor()

                // Downloading subtitles
                println("\uD83D\uDD0A Analyzing audio stream... ")
                val autoSubNodes = AutoSubUtils.getSubFor(inputVideo, videoLanguage)
                println("✔️ Audio analysis finished")
                println("\uD83C\uDFA5 Analyzing video stream...")
                val subAnalyzer = SubtitleAnalyzer(minTimelapseSourceLength, timelapseSpeed)
                val subReport = subAnalyzer.getReport(autoSubNodes)
                println("✔️ Video analysis finished")
                println("\uD83C\uDFB8 Analyzing BGM...")
                val bgmAgent = BgmAgent(
                    bgmFiles,
                    subReport.totalTimelapseDuration,
                    false
                )
                println("✔️ BGM analysis finished")


                val goodBgmFiles = bgmAgent.getBgmFiles()
                val bgmFile = if (goodBgmFiles.size > 1) {
                    println("\uD83C\uDF00 Merging BGM files...")
                    val mergedFile = AudioMerger(goodBgmFiles).merge()
                    println("✔️ BGM merging finished")
                    mergedFile
                } else {
                    goodBgmFiles.first()
                }

                println("⚛️ Preparing FFMPEG commands...")
                val commandCook = CommandCook(
                    inputVideo,
                    bgmFile,
                    subReport,
                    introDuration,
                    creditsDuration,
                    timelapseSpeed,
                    watermark,
                    watermarkTextColor,
                    watermarkFontSize,
                    watermarkBgColor,
                    watermarkBgOpacity,
                    introTitle,
                    introSubTitle,
                    creditsTitle,
                    creditsSubTitle,
                    titleFontSize,
                    subTitleFontSize,
                    titleColor,
                    subTitleColor,
                    fontFile,
                    bgColor,
                    highlightSection
                )

                val command = commandCook.prepareCommand()
                println("✔️ Commands prepared")
                println("↪ No of timelapses: ${subReport.timelapses.size}")
                if (subReport.timelapses.isNotEmpty()) {
                    println("↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️")
                }
                for (timelapse in subReport.timelapses) {
                    val duration = DateTimeUtils.getTimeFormatted(timelapse.end - timelapse.start)
                    println(
                        "\uD83D\uDD5B [${DateTimeUtils.getTimeFormatted(timelapse.start)} - ${DateTimeUtils.getTimeFormatted(
                            timelapse.end
                        )}] \uD83C\uDFA5 $duration \uD83D\uDC41️ ${timelapse.targetDuration.toInt()}sec"
                    )
                }
                if (subReport.timelapses.isNotEmpty()) {
                    println("↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️↔️")
                }

                val commandSh = File("${inputVideo.name}.sh")
                commandSh.delete()
                commandSh.writeText(command)
                println("\uD83D\uDCBE Command saved to ${commandSh.absolutePath}")

                println("⚒ Executing command...")
                ComplexCommandExecutor.executeCommand(
                    "sh \"${commandSh.absolutePath}\"",
                    true,
                    { true },
                    { false },
                    isSuppressError = true,
                    isReturnAll = true,
                    prefix = "\uD83D\uDCBF ",
                    isClearAfterFinish = true
                )
                println("✔️ Commands executed")
                println("\uD83C\uDF89 File saved to ${commandCook.outputFileName}")
                commandSh.delete()

                exitProcess(0)

            }
        } catch (e: Exception) {
            e.printStackTrace()
            exitProcess(0)
        }

    }
}