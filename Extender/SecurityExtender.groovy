/**
 * This code extends the basic build to include new files and new code.
 */

class MyData {
    String theFile
    String extMarker
    String srcInsert
}

def changes = []

// ******** New code changes ********
// ******** Change to delete videos and thumbnails ********
changes << new MyData(
    theFile: "MovementService.java",
    extMarker: """
            movement.setLastUpdate(new Date());
            movement.setDeleteFlag(boolVal);
            em.merge(movement);
""",
    srcInsert: """
            if (movement.getMovementVideoVersion().intValue() > 0) {
                String videoDir = System.getProperty("com.sw-builder.sync.app.video.dir");
                String movementVideoHome = videoDir + "Movement/MovementVideo/";
                File videoFile = new File(movementVideoHome + movement.getMovementVideoName() + ".mp4");
                videoFile.delete();
                String thumbnailDir = System.getProperty("com.sw-builder.sync.app.thumbnail.dir");
                String movementVideoThumbnailHome = thumbnailDir + "Movement/MovementVideo/";
                File thumbFile = new File(movementVideoThumbnailHome + movement.getCloudId() + "VidThumb" + movement.getMovementVideoVersion() + ".jpg");
                thumbFile.delete();
            }
            movement.setLastUpdate(new Date());
            movement.setDeleteFlag(boolVal);
            em.merge(movement);
"""
)

// ******** Process code changes ********
def normalize(String text) {
    text.replaceAll(/\r\n|\r/, "\n")
}

def processFiles(File dir, List<MyData> changes) {
    dir.eachFileRecurse { file ->
        if (!file.isFile()) return

        changes.each { data ->
            if (file.name == data.theFile) {
                def oldText = normalize(file.text)
                def marker  = normalize(data.extMarker)
                def insert  = normalize(data.srcInsert)
                def newText = oldText.replace(marker, insert)

                file.write(newText)

                if (!newText.contains(insert)) {
                    println "${data.theFile} missing changes"
                }
            }
        }
    }
}

processFiles(new File("../security"), changes)

// ******** Include new files ********
// Copy observer class
new File("../security/security-ejb/src/main/java/com/swBuilder/security/app/event/CheckMaxVideosObserver.java")
    .text = new File("./CheckMaxVideosObserver.java").text

// Copy logo
new File("../security_logo.png").withInputStream { src ->
    new File("../security/security-war/src/main/webapp/resources/gfx/logo.png").withOutputStream { dst ->
        dst << src
    }
}