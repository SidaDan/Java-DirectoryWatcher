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

/**
 * {@code Enum} which contains all available watch options for the {@code DirectoryWatcher} class.
 * <p>
 * The watch options define, how the {@code DirectoryWatcher} watches a directory. This can be either
 * {@code ROOT_ONLY} and {@code INCLUDE_SUB_DIRS}.
 * </p>
 *
 * @author Niklas Schultz
 * @version 0.1.0
 * @since 0.1.0
 */
public enum WatchOptions {

    /**
     * This option specifies for the {@code DirectoryWatcher} class to only watch the specified root directory for
     * changes. Changes inside potential sub directories are will not be detected.
     */
    ROOT_ONLY,

    /**
     * This option specifies for the {@code DirectoryWatcher} class to watch the specified root directory aswell as
     * all potential sub directories for changes.
     */
    INCLUDE_SUB_DIRS

}
