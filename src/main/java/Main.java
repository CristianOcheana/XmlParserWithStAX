import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import static java.nio.file.StandardWatchEventKinds.*;

public class Main {

    public static void main(String[] args){


        String inputPath = "src\\main\\resources\\input\\";
        String outputPath = "src\\main\\resources\\output\\";

        FileParser fileParser = new FileParser();
        FileWriter fileWriter = new FileWriter();

        WatchService watcher = null;

        try {
            //register watcher on input folder
            Path inputdir = new File(inputPath).toPath();
            watcher = FileSystems.getDefault().newWatchService();
            inputdir.register(watcher, ENTRY_CREATE);
            //listen for events - infinite loop
            while (true){
                try {
                    WatchKey key = watcher.take();

                    for (WatchEvent<?> event: key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();

                        if ( kind != ENTRY_CREATE)
                            continue; //interested only in create events
                        //new event = new file arrived in input folder
                        WatchEvent<Path> ev = (WatchEvent<Path>)event;
                        Path filename = ev.context();
                        //verify that the new file is an xml file
                        Path file = inputdir.resolve(filename);
                        if ( !Files.probeContentType(file).equals("text/xml")) {
                            System.out.println("Not an XML file. Skipping...");
                            continue;
                        }

                        //process the file
                        //get suffix for the output files name. i.e apple23.xml
                        String orderno = filename.toString().substring(filename.toString().indexOf('s') + 1, filename.toString().indexOf('.'));
                        //parse xml and get suppliers list with list of products
                        Collection<Supplier> suppliers = fileParser.processFile(file.toAbsolutePath().toString());
                        //write supplier xml
                        suppliers.forEach(supplier -> {
                            System.out.println("Writing file for: " + supplier.getSupplierName());
                            //order by timestamp and price descendant
                            Collections.sort(supplier.getProductList(), Comparator.comparing(Product::getTimestamp).thenComparing(Product::getPrice).reversed());
                            //write file
                            fileWriter.writeXMLFile(outputPath, orderno, supplier);
                        });
                    }
                    //reset event
                    if (!key.reset())
                        break;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

