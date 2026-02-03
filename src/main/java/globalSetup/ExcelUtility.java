package globalSetup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility extends GeneralMethods {

	public static FileInputStream fis;
	public static FileOutputStream fos;
	public static XSSFWorkbook wb;
	public static XSSFSheet sheet;
	public static XSSFRow row;
	public static XSSFCell cell;

	// this map stores the data which is to be stored in excel
	static Map<String, Object[]> data = new TreeMap<String, Object[]>();

	/**
	 * Utility to write in an excel sheet
	 * 
	 * @param sheet_Name : String : Name of new sheet
	 * @param sheet_Path : String : Path of the sheet
	 * @param data       : Map(String, Object[]) : Data to be written in sheet
	 * @return Void
	 **/

	public void write(String sheet_Name, String sheet_Path, Map<String, Object[]> data) throws IOException {
		wb = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet sheet = wb.createSheet(sheet_Name);

		// Iterate over data and write to sheet
		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset) {
			row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
			}
		}

		// Write the workbook in file system
		fos = new FileOutputStream(new File(sheet_Path));
		wb.write(fos);
		fos.close();
		System.out.println("******File Write Successful.******");
	}

	/**
	 * Utility to Update existing excel sheet
	 * 
	 * @param PATH  : String : Path of the sheet
	 * @param ID    : Integer : Id of the data in map
	 * @param KEY   : String : Map Key
	 * @param VALUE : String : Key value
	 * @return Void
	 **/
	public void update(String PATH, int ID, String KEY, String VALUE) {

		try {
			fis = new FileInputStream(new File(PATH));

			// Create Workbook instance holding reference to .xlsx file
			wb = new XSSFWorkbook(fis);

			// Get first/desired sheet from the workbook
			sheet = wb.getSheetAt(wb.getActiveSheetIndex());

			ExcelUtility Up = new ExcelUtility(ID, KEY, VALUE);
			// Get the count in sheet
			int rowCount = sheet.getLastRowNum() + 1;
			row = sheet.createRow(rowCount);
			System.out.println();
			Cell c1 = row.createCell(0);
			c1.setCellValue(Up.getId());
			Cell c2 = row.createCell(1);
			c2.setCellValue(Up.getFirstName());
			Cell c3 = row.createCell(2);
			c3.setCellValue(Up.getLastName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// Write the workbook in file system
			fos = new FileOutputStream(new File(PATH));
			wb.write(fos);
			fos.close();
			// System.out.println("******File Update Successfull******");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int id;
	private String col1;
	private String col2;

	public ExcelUtility() {
	}

	public ExcelUtility(int id, String first, String second) {
		super();
		this.id = id;
		this.col1 = first;
		this.col2 = second;
	}

	public String getFirstName() {
		return col1;
	}

	public void setFirstName(String first) {
		this.col1 = first;
	}

	public String getLastName() {
		return col2;
	}

	public void setLastName(String second) {
		this.col2 = second;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void updateSpecified(String FilePath, String xlsheet, int rowNum, int colNum, String updatedValue) {
		try {
			fis = new FileInputStream(new File(FilePath));
			wb = new XSSFWorkbook(fis);
			sheet = wb.getSheet(xlsheet);
			row = sheet.getRow(rowNum);
			if (row == null) {
				row = sheet.createRow(rowNum);
			}
			cell = row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
			} else {
				cell.setCellValue("");
			}
			cell.setCellValue(updatedValue);
			fis.close();
			fos = new FileOutputStream(new File(FilePath));
			wb.write(fos);
			wb.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (wb != null) {
					wb.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Utility to Get data from sheet
	 * 
	 * @param xlfile  : String : Path of the sheet
	 * @param xlsheet : String : Name of the sheet
	 * @param rowNum  : Integer : Row Number
	 * @param colnum  : String : Column Number
	 * @return String
	 **/
	public String getData(String xlfile, String xlsheet, int rowNum, int colnum) throws IOException {

		fis = new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fis);
		sheet = wb.getSheet(xlsheet);
		row = sheet.getRow(rowNum);
		cell = row.getCell(colnum);

		String data = "";
		try {
			DataFormatter formatter = new DataFormatter();

			if (cell.getCellType() == CellType.FORMULA) {
				FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
				CellType cellType = evaluator.evaluateFormulaCell(cell);
				switch (cellType) {
				case STRING:
					data = cell.getStringCellValue();
					break;
				case NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy");
						data = dateFormat.format(cell.getDateCellValue());
					} else {
						DecimalFormat df = new DecimalFormat("#.##");
						data = df.format(cell.getNumericCellValue());
					}
					break;
				case BOOLEAN:
					data = String.valueOf(cell.getBooleanCellValue());
					break;
				case BLANK:
				case ERROR:
					data = "";
					break;
				default:
					data = "";
					break;
				}
			} else {
				data = formatter.formatCellValue(cell);
			}

		} catch (Exception e) {
			data = "";
		} finally {
			wb.close();
			fis.close();
		}
		return data;
	}

	public static List<String> getExcelSheetHeaders(String filePath, int sheetIndex) throws IOException {
		List<String> headers = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream(new File(filePath));
				Workbook workbook = WorkbookFactory.create(fis)) {
			Sheet sheet = workbook.getSheetAt(sheetIndex);
			Row headerRow = sheet.getRow(0);
			if (headerRow != null) {
				for (Cell cell : headerRow) {
					headers.add(cell.getStringCellValue());
				}
			}
			return headers;
		}
	}

	public static File getMostRecentFile(String directoryPath) {
		File dir = new File(directoryPath);
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xlsx");
			}
		});
		if (files != null && files.length > 0) {
			return Arrays.stream(files).max(Comparator.comparingLong(File::lastModified)).orElse(null);
		}
		return null;
	}

	/**
	 * Utility to get total row count in a sheet
	 * 
	 * @param xlfile  : String : Path of the Excel sheet
	 * @param xlsheet : String : Name of the Excel sheet
	 * @return int : Total number of rows in the sheet
	 */
	public int getRowCount(String xlfile, String xlsheet) throws IOException {
		FileInputStream fis = new FileInputStream(xlfile);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		Sheet sheet = wb.getSheet(xlsheet);

		int rowCount = 0;
		for (Row row : sheet) {
			if (row != null && row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK) {
				rowCount++;
			}
		}

		wb.close();
		fis.close();

		return rowCount;
	}

}