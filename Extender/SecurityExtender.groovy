def changes = []

public class MyData {

  def theFile = ""

  def extMarker =
"""
"""
  def srcInsert =
"""
"""
}
MyData newData = new MyData()


// ******** Change to delete videos ******** 

newData = new MyData()
newData.theFile = "MovementService.java"
newData.extMarker =
"""
            movement.setLastUpdate(new Date());
            movement.setDeleteFlag(boolVal);
            em.merge(movement);
"""
newData.srcInsert =
"""
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
changes.add(newData)



// ******** Done ********


def dir = "../security"

def extFiles ( theDir, changes ) {

   def fileList = new File(theDir).list().toList()

   for ( i in fileList ) {

      def inFile = theDir + "/" + i
      def f1= new File(inFile)

      MyData myData = new MyData();

      if ( f1.isDirectory() ) {
         extFiles ( inFile, changes )
      } else {
//println(i)
        for (c in changes) {
          MyData theData = c
          if ( i.equals(theData.theFile) ) {
            def oldFile = new File(inFile).text
            def newMarker = theData.extMarker.replaceAll( "\\\n", "\\\r\\\n" )
            def newSrc = theData.srcInsert.replaceAll( "\\\n", "\\\r\\\n" )
            def newFile = oldFile.replace(newMarker, newSrc)
            new File(inFile).write(newFile)
            if (newFile.contains(newSrc) == false) { println(theData.theFile + " missing changes") }
          }

        }

      } 
   }
}

extFiles ( dir, changes )


def CheckMaxVideosObserver = new File("./CheckMaxVideosObserver.java").text
new File("../security/security-ejb/src/main/java/com/swBuilder/security/app/event/CheckMaxVideosObserver.java").write(CheckMaxVideosObserver)

def src = new File("../security_logo.png").newDataInputStream()
def dst = new File("../security/security-war/src/main/webapp/resources/gfx/logo.png").newDataOutputStream()
dst << src