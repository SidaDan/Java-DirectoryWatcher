# Java-DirectoryWatcher

A small library for watching directory changes.

## Simple Example

```java

class Example implements DirectoryWatchable {

  public static void main(String[] args){
    DirectoryWatcher watcher = new DirectoryWatcher(Paths.get("C:/"), new Example());
    watcher.startWatching();
  }

  @Override
  public void changeDetected(ChangedFile changedFile){
         // what kind of event occurred:
        System.out.println(changedFile.getChangeKind().name());
        // what file is the event referring to:
        System.out.println(changedFile.getAbsolutePath());
        // when did it happen:
        System.out.println(changedFile.getChangeTime());
        // you could also call the toString method to get all of the information above:
        System.out.println(changedFile.toString());
  }
}

```

## Lambda Example
You can also use a lambda expression to write a neat one liner

```java
new DirectoryWatcher(dirToWatch, changedFile -> System.out.println(changedFile.toString())).startWatching();
```
