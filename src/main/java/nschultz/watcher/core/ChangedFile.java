/*
Copyright (c) 2017 Niklas Schultz
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
DEALINGS IN THE SOFTWARE.
 */
package nschultz.watcher.core;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * The {@code {@link ChangedFile}} class represents a file that has been changed in a directory which is being watched
 * by the {@code {@link DirectoryWatcher}} class.
 * <br/>
 * Note that since this class contains a package private constructor it can not be initialized from outside the library.
 * This is because only the {@code {@link DirectoryWatcher}} should be able to create such an instance.
 * <br/>
 * <b>Also note that the {@code whenAsString} methods will NOT return a result that could be considered hundred percent
 * accurate. There very might well be some amount of delay between the occurrences of the change event and the result
 * of the {@code whenAsString} methods.</b>
 *
 * @author Niklas Schultz
 * @version 0.1.0
 * @since 0.1.0
 */
public class ChangedFile {

    private final Date changeTime;
    private final Path path;
    private final WatchEvent.Kind changeKind;

    /**
     * Creates a new instance of {@code {@link ChangedFile}}.
     *
     * @param path       the path of the file that has been changed
     * @param changeKind the change kind which gives info about what kind of change
     *                   happened to the file
     */
    ChangedFile(final Path path, final WatchEvent.Kind changeKind) {
        this.changeTime = new Date(System.currentTimeMillis());
        this.path = Objects.requireNonNull(path, "path must not be null");
        this.changeKind = Objects.requireNonNull(changeKind, "changeKind must not be null");
    }

    /**
     * Returns the time when the change for this file happened.
     *
     * @return the time of change
     */
    public final Date when() {
        return changeTime;
    }

    /**
     * Returns the time when the chane for this file happened. The returned {@code {@link String}} will
     * be formatted in this pattern: <b>yyyy-MM-dd HH:mm:ss</b>.
     *
     * @return the time when this file was changed
     */
    public final String whenAsString() {
        return whenAsString("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Returns the time when the change for this file happened.
     *
     * @param format the way the time will be formatted before it gets returned
     * @return the time when this file was changed
     */
    public final String whenAsString(final String format) {
        return new SimpleDateFormat(format).format(changeTime);
    }

    /**
     * Returns the name of the file that has been changed.
     *
     * @return the name
     */
    public final Path getName() {
        return path.getFileName();
    }

    /**
     * Returns the absolute path of the changed file.
     *
     * @return the absolute path
     */
    public final Path getAbsolutePath() {
        return path.toAbsolutePath();
    }

    /**
     * Returns the an {@code WatchEvent#Kind} which will give intel about what kind of change happened to the
     * file.
     *
     * @return the change kind that happened on the file
     */
    public final WatchEvent.Kind getChangeKind() {
        return changeKind;
    }

    /**
     * Returns true or false depending if the file was just created.
     *
     * @return true if the file was created; false otherwise
     */
    public final boolean isCreated() {
        return changeKind == ENTRY_CREATE;
    }

    /**
     * Returns true or false depending if the file was just modified.
     *
     * @return true if the file was modified; false otherwise
     */
    public final boolean isModified() {
        return changeKind == ENTRY_MODIFY;
    }

    /**
     * Returns true or false depending if the file was just deleted.
     *
     * @return true if the file was deleted; false otherwise
     */
    public final boolean isDeleted() {
        return changeKind == ENTRY_DELETE;
    }

    /**
     * Returns a {@code {@link String}} which represents this class.
     *
     * @return string representing this class
     */
    @Override
    public String toString() {
        return "ChangedFile{" +
                "path=" + path +
                ", changeKind=" + changeKind +
                ", changeTime=" + changeTime +
                '}';
    }
}
