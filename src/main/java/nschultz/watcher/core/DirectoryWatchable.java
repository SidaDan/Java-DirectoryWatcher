/*
Copyright (c) 2018 Niklas Schultz
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

import java.io.IOException;

/**
 * The {@code {@link DirectoryWatchable}} interface should be implemented by any class which wishes to
 * be notified when a directory change occurs.
 * <br/>
 * This interface has two methods. The {@code abstract changeDetected} method and the method
 * {@code failed} with a default implementation. The default implementation of the {@code failed} method will
 * simply print the stacktrace of the caught {@code {@link IOException}}.
 * <br/>
 * The {@code {@link DirectoryWatchable}} interface should be used together with the {@code {@link DirectoryWatcher}}
 * class.
 * <br/>
 * Since this interface is a <i>FunctionalInterface</i> it can also be used with the <i>lambda</i> syntax.
 *
 * @author Niklas Schultz
 * @version 0.1.0
 * @since 0.1.0
 */
@FunctionalInterface
public interface DirectoryWatchable {

    /**
     * Gets invoked when a change in the directory specified in the {@code {@link DirectoryWatcher}} class was detected.
     *
     * @param changedFile the file that has been changed
     */
    void changeDetected(ChangedFile changedFile);

    /**
     * Gets called in case an internal exception occurred. This could be because the {@code {@link DirectoryWatcher}}
     * did not have access rights to the specified directory.
     * </br
     * This method has a default implementation which will simply print a stacktrace of the exception instance to
     * the set error stream.
     *
     * @param ex an instance of the {@code {@link IOException}} that occurred
     *           while trying to watch the directory for changes
     */
    default void failed(IOException ex) {
        ex.printStackTrace(System.err);
    }
}
