package globalSetup;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PDFBox implements Base {

	public static void verify_eSign(PDDocument document) throws Exception {
		// Get all signatures
		int signee = 2;
		int signatureFound = 0;

		for (PDPage page : document.getPages()) {
			PDResources resources = page.getResources();
			for (COSName xObjectName : resources.getXObjectNames()) {
				if (resources.isImageXObject(xObjectName)) {
					PDImageXObject image = (PDImageXObject) resources.getXObject(xObjectName);
					boolean signPresent = (image.getWidth() + "x" + image.getHeight()).equalsIgnoreCase("438x140");
					if (signPresent) {
						signatureFound = signatureFound + 1;
					}
				}
			}
		}
		
		//verifying presence of all signatures
		if (signatureFound != signee) {
			throw new Exception("eSign Tag not replaced! Signee:- " + signee + ", " + "Signatures found in document:- "
					+ signatureFound);
		}
	}
}
