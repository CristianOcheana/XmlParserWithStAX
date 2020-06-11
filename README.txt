Uses StAX for read/write XML and WatchService to monitor when files appear in the input folder.

How to run:
Start application.
Put input file in folder: src\main\resources\input\
Application runs forever.
Output files will be created in src\main\resources\output

What it does:
Creates WatchService to monitor new files in input folder (listens only to ENTRY_CREATE event). Files will remain in input folder after processing. Only new files are picked up. If any files exists in input folder before starting application, they will be ignored.
Enters an infinite loop where it waits for events.
When a new event is picked up and it is ENTRY_CREATE event it will:
-	Verifies it is an xml file
-	Extracts order number from file name
-	Processes the file using fileParser.processFile(file name):
o	Reads file using StAX and creates 2 types of objects:
	Supplier which has the supplier name and the list of products
	Product which has the product info
o	Creates a map of supplier name and Supplier object (which holds all products for the supplier)
o	Returns list of suppliers
-	For each supplier returned by file processFile function it will:
o	 order the product list by timestamp and price descendant.
o	Write xml file using StAX -> function: fileWriter.writeXMLFile
