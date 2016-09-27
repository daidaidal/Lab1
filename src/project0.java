import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class project0
{
	static String equation;
	static String control;

	public static void main(String[] args)
	{
		while (true)
			expression();
	}

	static void expression()
	{

		System.out.println(">");
		Scanner in = new Scanner(System.in);
		String temp = in.nextLine();
		if (temp.charAt(0) == '!')
		{
			control = temp;
			if (control.charAt(1) == 'd')
				derivative();
			else if (control.charAt(1) == 's')
				simplify();
		}
		else
		{
			temp = temp.replace(" ", "");
			temp = temp.replace("	", "");
			equation = temp;
			char c;
			for (int i = 0; i < equation.length(); i++)
			{
				c = equation.charAt(i);
				if (!((c >= 48 && c <= 57) || (c >= 97 && c <= 122) || c == '+' || c == '*' || c == '-'))
				{
					equation = null;
					System.out.println("Please check the input!");
					return;
				}
			}
			System.out.println(equation);
		}
	}

	static void simplify()
	{
		// a+b+c
		// !simplify a=2
		String eq = equation;
		String front = "";
		String number = "";
		String order = "";
		int f = 0;
		int e = 0;
		int j = 0;

		int num = 0;
		int nummu = 1;
		int numsum = 0;

		// 判定前一个是数字还是字母
		boolean n = false;
		boolean a = false;

		String ab = null;
		String absum = null;
		String stringsum = "";
		// replace

		front = control.substring(10, control.indexOf("="));
		number = control.substring(control.indexOf("=") + 1);

		String abc = "";
		int h = 0;
		boolean flag = false;
		while (h < eq.length())
		{
			if (eq.charAt(h) >= 'a' && eq.charAt(h) <= 'z')

			{
				abc = abc + eq.charAt(h);
				if (h == eq.length() - 1 && abc.equals(front))
				{
					flag = true;
					eq = eq.substring(0, h - abc.length() + 1) + number;
					break;
				}
			}
			else
			{
				a = false;
				if (abc.equals(front))
				{
					flag = true;
					eq = eq.substring(0, h - abc.length()) + number + eq.substring(h);
					h = h - abc.length() + number.length();
				}
				abc = "";
			}
			h++;
		}
		a = false;
		eq = eq + "+";
		if (eq != null)
		{
			if (eq.charAt(0) == '-')
				order = order + "-";
			else
				order = order + "+";
			for (int i = 1; i < eq.length(); i++)
				if (eq.charAt(i) == '+')
					order = order + "+";
				else if (eq.charAt(i) == '-')
					order = order + "-";
			j = 1;
			f = 0;
			while (true)
			{
				e = eq.indexOf(String.valueOf(order.charAt(j)));
				j++;
				front = eq.substring(0, e);
				front = front + "*";
				if (j < order.length())
					eq = eq.substring(e + 1);
				nummu = 1;
				num = 1;
				ab = "";
				absum = "";
				for (int i = 0; i < front.length(); i++)
				{
					if (front.charAt(i) >= '0' && front.charAt(i) <= '9')
					{
						if (n == true)
							num = num * 10 + (front.charAt(i) - '0');
						else
						{
							n = true;
							num = front.charAt(i) - '0';
						}
					}
					else if (front.charAt(i) >= 'a' && front.charAt(i) <= 'z')
					{
						if (a == true)
							ab = ab + front.charAt(i);
						else
						{
							a = true;
							ab = "" + front.charAt(i);
						}
					}
					else
					{
						if (n == true)
							nummu = nummu * num;
						if (a == true)
							absum = absum + '*' + ab;
						n = false;
						a = false;
					}
				}
				// 纯字母
				if (nummu == 1 && absum != "")
					front = absum.substring(1);
				else
					front = nummu + absum;
				// 纯数字
				if (absum == "")
				{
					front = "";
					if (order.charAt(j - 2) == '+')
						numsum = numsum + nummu;
					else if (order.charAt(j - 2) == '-')
						numsum = numsum - nummu;
				}
				else
					stringsum = stringsum + order.charAt(j - 2) + front;
				if (j == order.length())
					break;
			}
			stringsum = numsum + stringsum;
			if (stringsum.charAt(0) == '+')
				stringsum = stringsum.substring(1);
			if (stringsum.charAt(0) == '0')
				stringsum = stringsum.substring(2);

			stringsum = reorganize(stringsum);
			if (flag == true)
				System.out.println(stringsum);
			else
				System.out.println("Error,no variable!");
		}
	}

	static String unitsort(String substr)// unit排序
	{
		String sorted = "";
		List<String> list = new ArrayList<String>();
		String unit = "";
		int prepos = 0;
		for (int i = 0; i < substr.length(); i++)
		{
			if (substr.charAt(i) == '*' || i + 1 == substr.length())
			{
				if (i + 1 == substr.length())// 得到最后一个unit
				{
					unit = substr.substring(prepos, i + 1);
					prepos = 0;
				}
				else
				{
					unit = substr.substring(prepos, i);
					prepos = i + 1;
				}
				list.add(unit);
			}
		}
		list.sort(null);
		for (int i = 0; i < list.size(); i++)
		{
			sorted += list.get(i);
			sorted += "*";
		}
		if (sorted != "")
			sorted = sorted.substring(0, sorted.length() - 1);
		return sorted;
	}

	static String reorganize(String result)// 整理
	{
		int prepos = 0;
		int j = 0;
		int num;
		boolean isnum = false;
		boolean negative = false;
		String neweq = "";
		String temp;
		String nstr = "";
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < result.length(); i++)
		{
			if ((result.charAt(i) == '+' || result.charAt(i) == '-' || i + 1 == result.length()))
			{
				if (i + 1 == result.length())
				{
					temp = result.substring(prepos, i + 1);
					prepos = 0;
				}
				else
				{
					temp = result.substring(prepos, i);
					prepos = i + 1;
				}
				for (j = 0; j <= temp.length() - 1 && temp.charAt(j) != '*'; j++)
				{
					if ((temp.charAt(j) >= 48 && temp.charAt(j) <= 57))
						isnum = true;
				}
				if (isnum)
				{
					num = Integer.parseInt(temp.substring(0, j));
					if (j + 1 < temp.length())
						temp = temp.substring(j + 1);
					else
						temp = "";
				}
				else
					num = 1;
				if (negative)
				{
					num *= -1;
					negative = false;
				}
				temp = unitsort(temp);
				if (!map.containsKey(temp))
					map.put(temp, num);
				else
				{
					num += map.get(temp);
					map.put(temp, num);
				}
				j = 0;
				isnum = false;
				if (result.charAt(i) == '-')
					negative = true;
			}
		}
		Iterator<Entry<String, Integer>> iter = map.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iter.next();
			String str = entry.getKey().toString();
			Integer n = (Integer) entry.getValue();
			if (n > 0 && n != 1)
			{
				nstr = "+" + n.toString();
				neweq += nstr;
				if (str != "")
				{
					neweq += "*";
					neweq += str;
				}
			}
			else if (n < 0)
			{
				nstr = n.toString();
				neweq += nstr;
				if (str != "")
				{
					neweq += "*";
					neweq += str;
				}
			}
			else if (n == 1)
			{
				if (str == "")
				{
					nstr = "+" + n.toString();
					neweq += nstr;
				}
				else
				{
					str = "+" + str;
					neweq += str;
				}
			}
		}
		if (neweq.length() == 0)
			neweq = "0";
		if (neweq.length() != 0 && neweq.charAt(0) == '+')
			neweq = neweq.substring(1);

		/*
		 * if (n > 0) nstr = "+" + n.toString(); if (n != 1) { if (n != 0) neweq
		 * += nstr; neweq += "*"; } if (n == 0) str = ""; if (str == "") neweq =
		 * neweq.substring(0, neweq.length() - 1); neweq += str; } if (neweq ==
		 * "") neweq = "0"; if (neweq != "" && neweq.charAt(0) == '+') neweq =
		 * neweq.substring(1);
		 */

		return neweq;
	}

	static void derivative()
	{
		int times = 0;// 次数
		String var = null;// 要求导的变量
		for (int i = 0; i < control.length(); i++)
		{
			if (control.charAt(i) == 'd')
				times++;
			if (times == 2)
			{
				times = 0;
				var = control.substring(i + 1, control.length());
				break;
			}
		}
		String substr = null, unit = null;// 将整个式子按+分割后的串；将substr按*分割后的串
		int prepos_main = 0, prepos_sub = 0;// 分割整个式子时前一分割位置的标记；分割substr时前一分割位置的标记
		int coe = 1, temp;// substr的系数；临时变量
		int positive = 1;// ‘+’、‘-’号标记，1：+ 0：- 2：空
		String result = "", tem = "";// 最终求导的结果；临时变量
		boolean e_hasvar = false, u_hasvar = false;// 整个式子中是否包含var；unit是否是var
		for (int i = 0; i < equation.length(); i++)
		{
			// 按加减号分割字符串
			if ((equation.charAt(i) == '+' || equation.charAt(i) == '-' || i + 1 == equation.length()) && (i != 0))
			{
				if (i + 1 == equation.length())// 得到最后一个substr
				{
					substr = equation.substring(prepos_main, i + 1);
					prepos_main = 0;
				}
				else
				{
					substr = equation.substring(prepos_main, i);
					prepos_main = i + 1;
				}
				if (equation.charAt(i) == '+')
					positive = 1;
				else if (equation.charAt(i) == '-')
					positive = 0;
				else
					positive = 2;
				for (int j = 0; j < substr.length(); j++)
				{ // 按乘号分割子字符串
					if (substr.charAt(j) == '*' || j + 1 == substr.length())
					{
						if (j + 1 == substr.length())// 得到最后一个unit
						{
							unit = substr.substring(prepos_sub, j + 1);
							prepos_sub = 0;
						}
						else
						{
							unit = substr.substring(prepos_sub, j);
							prepos_sub = j + 1;
						}
						if (unit.charAt(0) >= 48 && unit.charAt(0) <= 57)// 数字
						{
							temp = Integer.parseInt(unit);
							coe *= temp;
						}
						else if (unit.equals(var))// var
						{
							times++;
							e_hasvar = true;
							u_hasvar = true;
						}
						else// 其他
						{
							tem += unit;
							tem += "*";
						}
					}
				}
				if (times > 0)// substr的次数大于0
				{
					coe *= times;
					if (coe != 1)
					{
						result += String.valueOf(coe);
						if (tem != "")
						{
							result += "*";
							tem = tem.substring(0, tem.length() - 1);
							result += tem;
						}
					}
					else
					{
						if (tem != "")
						{
							tem = tem.substring(0, tem.length() - 1);
							result += tem;
						}
						else
							result += "1";
					}
					if (times != 1)
					{
						for (int k = times; k > 1; k--)
						{
							result += "*";
							result += var;
						}
					}
				}
				times = 0;
				coe = 1;
				tem = "";

				if (positive == 1 && u_hasvar)
					result += "+";
				else if (positive == 0 && result != "" && result.charAt(result.length() - 1) != '-'
						&& result.charAt(result.length() - 1) != '+')
					result += "-";
				u_hasvar = false;
			}
		}
		if (result.length() != 0
				&& (result.charAt(result.length() - 1) == '+' || result.charAt(result.length() - 1) == '-'))
			result = result.substring(0, result.length() - 1);
		result = reorganize(result);
		if (e_hasvar)
			System.out.println(result);
		else
			System.out.println("Error,no variable!");
	}
}
//add a change :)