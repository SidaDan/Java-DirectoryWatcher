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
package nschultz.example;

import nschultz.watcher.core.ChangedFile;
import nschultz.watcher.core.DirectoryWatchable;
import nschultz.watcher.core.DirectoryWatcher;

import java.nio.file.Paths;

public class SimpleDemo_1 implements DirectoryWatchable {

    public static void main(String[] args) {
        // create an instance of the DirectoryWatcher
        DirectoryWatcher dirWatcher = new DirectoryWatcher(Paths.get("C:/"), new SimpleDemo_1());
        // start watching
        dirWatcher.startWatching();
    }

    @Override
    public void changeDetected(ChangedFile changedFile) {
        // what kind of event occurred:
        System.out.println(changedFile.getChangeKind().name());
        // what file is the event referring to:
        System.out.println(changedFile.getAbsolutePath());
        // whenAsString did it happen:
        System.out.println(changedFile.whenAsString());
        // you could also call the toString method to get all of the information above:
        System.out.println(changedFile.toString());
    }
}
