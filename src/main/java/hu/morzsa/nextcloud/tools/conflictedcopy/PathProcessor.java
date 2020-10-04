package hu.morzsa.nextcloud.tools.conflictedcopy;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class PathProcessor {

    private String startPath;
    private boolean isRecursive = false;

    public PathProcessor(String path, Boolean recursive){
        if (recursive != null){
            isRecursive = recursive;
        }
        startPath = path;
    }

    public void start(){
        System.out.println("Scan folder for conflicted copies:");
        if (isRecursive){
            recursiveProcessing(new File(startPath));
        }else{
            replaceConflictedFiles(new File(startPath));
        }
        System.out.println("Process ready!");
    }

    private void recursiveProcessing(File path){
        List<File> folders = listFolderForFolder(path);
        for (File dir:folders) {
            recursiveProcessing(dir);
        }
        replaceConflictedFiles(path);
    }

    private void replaceConflictedFiles(File path){
        System.out.print(path.getPath()+": ");
        List<FilterableFile> files = listFilesForFolder(path);
        if (files.size() > 0){

            List<FilterableFile> conflicts = files.stream()
                    .filter(FilterableFile::isConflictedFile)
                    .collect(Collectors.toList());
            System.out.println(" [Conflicted files: "+conflicts.size()+"]");
            while (conflicts.size() > 0){
                replaceSameFileAtOnce(files, conflicts.get(0).getOriginalName(), conflicts);
            }

        }
        System.out.println("    OK");
    }

    private List<FilterableFile> replaceSameFileAtOnce(List<FilterableFile> allFile, String fileOriginalName, List<FilterableFile> conflicts){
        List<FilterableFile> sameFiles = conflicts.stream()
                .filter((f) -> f.getOriginalName().equals(fileOriginalName))
                .collect(Collectors.toList());
        FilterableFile freshFile = null;
        if (sameFiles.size() > 1){
            sameFiles.sort(Comparator.comparing(FilterableFile::getDateTime));
            freshFile = sameFiles.get(sameFiles.size()-1);
        }else{
            freshFile = sameFiles.get(0);
        }
        FilterableFile finalFreshFile = freshFile;
        FilterableFile originalFile = allFile.stream()
                .filter(f -> f.getName().equals(finalFreshFile.getOriginalName()))
                .findAny()
                .orElse(null);
        if (originalFile != null && replaceFile(allFile, originalFile, freshFile)){
            for (FilterableFile f:sameFiles) {
                conflicts.remove(f);
                allFile.remove(f);
                if (f.exists()){
                    f.delete();
                }
            }
        }
        return conflicts;
    }

    @NotNull
    private boolean replaceFile(List<FilterableFile> allFile, FilterableFile oldFile, FilterableFile newFile){
        if (oldFile.getName().equals(newFile.getOriginalName())){
            try {
                Files.move(newFile.toPath(), oldFile.toPath(), StandardCopyOption.ATOMIC_MOVE);
                allFile.remove(oldFile);
                System.out.println("    Replaced file: "+oldFile.getPath());
                return true;
            } catch (IOException e) {
                System.out.println("    Can't delete file to replace with conflicted one: "+oldFile.getPath());
            }
        }
        return false;
    }

    private ArrayList<FilterableFile> listFilesForFolder(File folder) {
        ArrayList<FilterableFile> files = new ArrayList<FilterableFile>();
        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()) {
                files.add(new FilterableFile(fileEntry.getPath()));
            }
        }
        return files;
    }

    private ArrayList<File> listFolderForFolder(File folder) {
        ArrayList<File> folders = new ArrayList<File>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                folders.add(fileEntry);
            }
        }
        return folders;
    }

}
