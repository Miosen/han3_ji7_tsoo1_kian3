package cc.adjusting.piece;

import java.util.Vector;

import cc.moveable_type.rectangular_area.RectangularArea;

/**
 * 整合包圍所需平推的物件，傳入，會先用包圍模組大概調整內部活字，大概平推一次、精細平推一次，最後再用包圍模組再調整。
 * 
 * @author Ihc
 */
public class 平推包圍調整工具
{
	/** 使用此包圍工具的調整工具，並使用其自身合併相關函式 */
	protected MergePieceAdjuster 調整工具;
	/** 主要使用的包圍模組 */
	protected Vector<縮放接合模組> 包圍模組列;
	/** 主要使用的包圍工具 */
	protected Vector<二元搜尋貼合工具> 包圍工具列;
	/** 平推過程中要用到的二元調整工具 */
	protected Vector<二元搜尋貼合工具> 平推工具列;
	/** 平推過程中要用到的平推黏合模組 */
	protected Vector<平推黏合模組> 平推模組列;

	/**
	 * 建立平推包圍調整工具，預設先上下再左右平推。
	 * 
	 * @param 包圍模組
	 *            主要使用的包圍模組
	 * @param 調整工具
	 *            使用此包圍工具的調整工具，並使用其自身合併相關函式
	 */
	public 平推包圍調整工具(MergePieceAdjuster 調整工具, 縮放接合模組 包圍模組)
	{
		this(調整工具, 包圍模組, true);
	}

	/**
	 * 建立平推包圍調整工具。
	 * 
	 * @param 調整工具
	 *            使用此包圍工具的調整工具，並使用其自身合併相關函式
	 * @param 包圍模組
	 *            主要使用的包圍模組
	 * @param 先上下後左右
	 *            是否先上下再左右平推，若否則相反
	 */
	public 平推包圍調整工具(MergePieceAdjuster 調整工具, 縮放接合模組 包圍模組, boolean 先上下後左右)
	{
		this(調整工具, 包圍模組, 包圍模組, 先上下後左右);
	}

	/**
	 * 建立平推包圍調整工具。
	 * 
	 * @param 調整工具
	 *            使用此包圍工具的調整工具，並使用其自身合併相關函式
	 * @param 粗略包圍模組
	 *            平推前使用的包圍模組
	 * @param 貼合包圍模組
	 *            平推後使用的包圍模組
	 * @param 先上下後左右
	 *            是否先上下再左右平推，若否則相反
	 */
	public 平推包圍調整工具(MergePieceAdjuster 調整工具, 縮放接合模組 粗略包圍模組, 縮放接合模組 貼合包圍模組,
			boolean 先上下後左右)
	{
		this.調整工具 = 調整工具;
		this.包圍模組列 = new Vector<縮放接合模組>();
		包圍模組列.add(粗略包圍模組);
		包圍模組列.add(貼合包圍模組);
		包圍工具列 = new Vector<二元搜尋貼合工具>();
		包圍工具列.add(new 二元搜尋間隔工具(4.0)); // TODO 人工參數
		包圍工具列.add(new 二元搜尋貼合工具());

		平推工具列 = new Vector<二元搜尋貼合工具>();
		平推工具列.add(new 二元搜尋間隔工具(4.0)); // TODO 人工參數
		平推工具列.add(new 二元搜尋間隔工具(0.5));
		平推模組列 = new Vector<平推黏合模組>();
		if (先上下後左右)
		{
			平推模組列.add(new 上推黏合模組(調整工具));
			平推模組列.add(new 下推黏合模組(調整工具));
			平推模組列.add(new 左推黏合模組(調整工具));
			平推模組列.add(new 右推黏合模組(調整工具));
		}
		else
		{
			平推模組列.add(new 左推黏合模組(調整工具));
			平推模組列.add(new 右推黏合模組(調整工具));
			平推模組列.add(new 上推黏合模組(調整工具));
			平推模組列.add(new 下推黏合模組(調整工具));
		}
	}

	/**
	 * 將活字物件的調整後，並回傳。
	 * 
	 * @param 活字物件
	 *            要調整的物件
	 * @return 活字物件調整後結果
	 */
	public RectangularArea[] 產生調整後活字(RectangularArea[] 活字物件)
	{
		活字寬度資訊 舊活字寬度資訊 = 調整工具.取得活字寬度資訊(活字物件[1]);
		包圍工具列.elementAt(0).執行(包圍模組列.elementAt(0), 活字物件);
		活字物件 = 包圍模組列.elementAt(0).取得調整後活字物件();
		for (二元搜尋貼合工具 工具 : 平推工具列)
			for (平推黏合模組 模組 : 平推模組列)
			{
				工具.執行(模組, 活字物件);
				活字物件 = 模組.取得調整後活字物件();
			}
		包圍工具列.elementAt(1).執行(包圍模組列.elementAt(1), 活字物件);
		活字物件 = 包圍模組列.elementAt(1).取得調整後活字物件();
		調整工具.依寬度資訊調整活字(活字物件[1], 舊活字寬度資訊);
		return 活字物件;
	}
}
