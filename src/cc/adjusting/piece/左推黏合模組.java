package cc.adjusting.piece;

import java.awt.geom.AffineTransform;

import cc.moveable_type.rectangular_area.平面幾何;
/**
 * 讓第二個活字往左延伸的模組，碰到第一個活字或是邊界即停止。
 * 
 * @author Ihc
 */
public class 左推黏合模組 extends 平推黏合模組
{
	/**
	 * 建立左推黏合模組
	 * 
	 * @param 調整工具
	 *            使用此模組的調整工具，並使用其自身合併相關函式
	 */
	public 左推黏合模組(MergePieceAdjuster 調整工具)
	{
		super(調整工具);
	}

	@Override
	public double 下限初始值()
	{
		return insidePiece.字範圍().getWidth();
	}

	@Override
	public double 上限初始值()
	{
		return outsidePiece.字範圍().getWidth();
	}

	@Override
	public boolean 活字是否太接近()
	{
		return super.活字是否太接近()
				|| outsidePiece.字範圍().getMinX() > temporaryPiece
						.字範圍().getMinX();
	}

	@Override
	public void 變形處理(double middleValue)
	{
		temporaryPiece = new 平面幾何(insidePiece);
		AffineTransform affineTransform = 調整工具.getAffineTransform(middleValue
				/ insidePiece.字範圍().getWidth(), 1.0);
		temporaryPiece.縮放(affineTransform);
		temporaryPiece.徙(insidePiece.字範圍().getMaxX()
				- temporaryPiece.字範圍().getMaxX(), insidePiece
				.字範圍().getMaxY()
				- temporaryPiece.字範圍().getMaxY());
		return;
	}
}
