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


import nschultz.watcher.filters.AcceptEverythingPredicate;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * The {@code {@link DirectoryWatcher}} is used to watch a directory for changes. These
 * changes could either be that a file has been created, deleted or modified.
 * <br/>
 * To use this class simply create an instance of it, specify the directory that you wish to
 * watch and pass a {@code {@link WatchEventCallback }} instance you wish to notify about those changes.
 * After the initializing of this class you can use the {@code startWatching} method to start and the
 * {@code stopWatching} method to stop the {@code {@link DirectoryWatcher}} again. If the {@code stopWatching}
 * method is not invoked the {@code {@link DirectoryWatcher}} will keep watching the specified directory.
 * <br/>
 * Note that this watcher does not look inside directories in the specified folder for changes. That means only the root
 * directory will get watched for changes.
 *
 * @author Niklas Schultz
 * @version 0.1.0
 * @since 0.1.0
 */
public class DirectoryWatcher implements Runnable {

    private final Path dirToWatch;
    private final WatchEventCallback callback;
    private Predicate<ChangedFile> filter = new AcceptEverythingPredicate();
    private ExecutorService dirWatcherThread;
    private volatile boolean isWatching = false;

    /**
     * Creates a new instance of {@code {@link DirectoryWatcher}}.
     *
     * @param dirToWatch the directory that will be watched for changes
     * @param callback   the WatchEventCallback instance that wants to get
     *                   notified about changes on the specified directory
     * @throws IllegalArgumentException if the given path points to a file that is not a directory
     */
    public DirectoryWatcher(final Path dirToWatch, final WatchEventCallback callback) {
        this.dirToWatch = Objects.requireNonNull(dirToWatch, "dirToWatch must not be null");
        this.callback = Objects.requireNonNull(callback, "callback must not be null");
        if (!Files.isDirectory(dirToWatch)) {
            throw new IllegalArgumentException("dirToWatch must be a directory");
        }
    }

    /**
     * Starts watching the specified directory for changes. In case the {@code {@link DirectoryWatcher}}
     * has already been started this call will have no effect.
     * </br>
     * Note that this method can also be used to restart the {@code {@link DirectoryWatcher}} after
     * the {@code stopWatching} method was used to stop the watcher.
     */
    public synchronized void startWatching() {
        if (!isWatching) {
            isWatching = true;
            dirWatcherThread = Executors.newSingleThreadExecutor();
            dirWatcherThread.execute(this);
        }
    }

    /**
     * Stops watching the specified directory for changes. In case the {@code {@link DirectoryWatcher}} has
     * already been stopped this call will have no effect.
     * <br/>
     * Note that the old thread that did the watching will be destroyed if it was running after this call returns.
     */
    public synchronized void stopWatching() {
        if (isWatching) {
            isWatching = false;
            dirWatcherThread.shutdownNow();
        }
    }

    /**
     * Sets the filter for this {@code DirectoryWatcher} instance. The filter determines which files are getting
     * reported when a change occurs. The default filter is set to
     * {@code {@link AcceptEverythingPredicate }} which means all files are getting reported.
     *
     * @param filter the filter
     * @throws NullPointerException if given Predicate is equal to <code>null</code>
     */
    public void setFilter(Predicate<ChangedFile> filter) {
        this.filter = Objects.requireNonNull(filter);
    }

    @Override
    public void run() {
        try (final WatchService watchService = dirToWatch.getFileSystem().newWatchService()) {
            dirToWatch.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

            while (isWatching) {
                final WatchKey watchKey = watchService.take();
                if (!isWatching) {
                    return;
                }
                pollEvents(watchKey);
            }
        } catch (IOException ex) {
            callback.failed(ex);
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
    }

    private void pollEvents(final WatchKey watchKey) {
        for (final WatchEvent watchEvent : watchKey.pollEvents()) {
            final WatchEvent.Kind kind = watchEvent.kind();
            if (kind == OVERFLOW) {
                continue;
            }

            final ChangedFile changedFile = new ChangedFile(constructChangedFilePath(watchEvent), kind);
            if (filter.test(changedFile)) {
                callback.onChangeDetected(changedFile);
            }

            if (!watchKey.reset()) {
                break;
            }
        }
    }

    private Path constructChangedFilePath(WatchEvent watchEvent) {
        return dirToWatch.resolve((Path) watchEvent.context());
    }
}