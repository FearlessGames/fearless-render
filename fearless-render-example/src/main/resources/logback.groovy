import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.FileAppender

import static ch.qos.logback.classic.Level.DEBUG

appender("FILE", FileAppender) {
    file = "fearless-render.log"
    append = false
    encoder(PatternLayoutEncoder) {
        pattern = "%level %logger - %msg%n"
    }
}


appender("GL", FileAppender) {
    file = "gl.log"
    append = false
    encoder(PatternLayoutEncoder) {
        pattern = "%level %logger - %msg%n"
    }
}


logger("se.fearlessgames.fear.gl.LoggingFearGl", DEBUG, ["GL"])
root(DEBUG, ["FILE"])