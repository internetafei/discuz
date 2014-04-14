package service;
public class Board {
	public static String[][] getBoardInfo(){
		String board[][]=new String[16][3];  //老id,名字，新id
		int index=0;
	 	board[index][0]="9";
		board[index][1]="开箱分享";
		board[index][2]="2";
		index++;
		board[index][0]="39";
		board[index][1]="购机咨询";
		board[index][2]="41";
		index++;
		board[index][0]="40";
		board[index][1]="建议反馈";
		board[index][2]="42";
		index++;
		board[index][0]="48";
		board[index][1]="安卓新闻";
		board[index][2]="47";
		index++;
		board[index][0]="68";
		board[index][1]="数码交流";
		board[index][2]="48";
		index++;		
		board[index][0]="56";
		board[index][1]="售后服务";
		board[index][2]="43";
		index++;
		board[index][0]="58";
		board[index][1]="用户反馈";
		board[index][2]="44";
		index++;
		board[index][0]="59";
		board[index][1]="物流查询";
		board[index][2]="46";
		index++;
		board[index][0]="60";
		board[index][1]="功能需求";
		board[index][2]="45";
		index++;
		board[index][0]="61";
		board[index][1]="售后活动";
		board[index][2]="49";
		index++;
		board[index][0]="62";
		board[index][1]="热闹活动";
		board[index][2]="50";
		index++;
		board[index][0]="63";
		board[index][1]="软件分享";
		board[index][2]="51";
		index++;
		board[index][0]="64";
		board[index][1]="玩机技巧";
		board[index][2]="52";
		index++;
		board[index][0]="69";
		board[index][1]="功能需求";
		board[index][2]="53";
		index++;
		board[index][0]="65";
		board[index][1]="铃声分享";
		board[index][2]="54";
		index++;
		board[index][0]="66";
		board[index][1]="壁纸分享";
		board[index][2]="55";
		return board;
	}
	public static void main(String[] args) {
		String[][] board = getBoardInfo();
		for (int i = 0; i < board.length; i++) {
			System.out.print( board[i][2]+",");
		}
	}
}
