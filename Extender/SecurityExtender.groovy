/**
 * NOTE: The code in this script was created on Windows.
 * Groovy multiline strings always use \n internally.
 * We normalize both the file content and the markers to \n before replacing
 * so the match succeeds whether the Java sources have \n or \r\n.
 */

class MyData {
    String theFile
    String extMarker
    String srcInsert
}

def changes = []

// ******** Change to delete videos ********
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
// ******** Done ********

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

// Copy observer class
new File("../security/security-ejb/src/main/java/com/swBuilder/security/app/event/CheckMaxVideosObserver.java")
    .text = new File("./CheckMaxVideosObserver.java").text

// Copy logo
new File("../security_logo.png").withInputStream { src ->
    new File("../security/security-war/src/main/webapp/resources/gfx/logo.png").withOutputStream { dst ->
        dst << src
    }
}