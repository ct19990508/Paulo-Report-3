package com.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bean.ExpressageBean;
import com.bean.MemberBean;
import com.bean.PermissionBean;
import com.bean.RoleBean;
import com.bean.UserToExpressageBean;
import com.util.Common;

public class ExpressageManagerServlet extends HttpServlet {


	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 String action = request.getParameter("action");
		 HttpSession session = request.getSession();
		 //��ѯ�����������û���id��ѯ������
		 
		 
		 if(Common.isEmpty(action))
		 {
	         Integer  userid2=(Integer) session.getAttribute("userid");//session����ǿ���ͣ�ֻ�ܴ洢Ϊinteger ��Ҫ����
             int userid=userid2;
		
         //�����û���id��ѯ���û�������������Ϣ
         ExpressageBean ex=new ExpressageBean();
         ExpressageBean[] exlist= ex.getAllByUserId(userid);
         for(ExpressageBean extest:exlist)
         {
         	System.out.println(extest.toString());
         	
         }
         
        request.setAttribute("exlist", exlist);
        request.getRequestDispatcher("./member/prep/index.jsp").forward(request, response);		
		}
		else if(action.equals("update"))
		{
			//����ת������ҳ��׼������
			//����id����õ�ǰ��Ϣ
			int id=Integer.parseInt(request.getParameter("id"));
			ExpressageBean ex=new ExpressageBean();
		    ex=ex.getOneByUserId(id);
			
		    request.setAttribute("ex", ex);
			request.getRequestDispatcher("./member/prep/up.jsp?id="+id).forward(request, response);
					
		}
		else if(action.equals("Collect"))
		{

			//��ѯ����ǰ�û���id
		    //�����û���id�������ѯ�����еĿ����Ϣ 
			//���ҷ���
			String member=(String)session.getAttribute("member"); 
        	if(member==null){

        		response.sendRedirect("./login.jsp");
        	
        	}

        	else
        	{
        MemberBean mb=new MemberBean();
        int userid=mb.getId(member);
        
		
        ExpressageBean ex=new ExpressageBean();
        ExpressageBean[] exlist= ex.getExpressageToUser(userid);
        request.setAttribute("exlist", exlist);
        
		request.getRequestDispatcher("./admin/hzp/type4.jsp").forward(request, response);

        	}
		    
		    
		}
		else if(action.equals("dq"))
		{
              //��� ��ǰ�û���id
			  //��� ��ǰ�����Ϣ��id
			  //�����߷ֱ����

			   //����Ҫ���û���id����
			
		
               //�����û�������û���id
            	String member=(String)session.getAttribute("member"); 
            	if(member==null){

            		response.sendRedirect("./login.jsp");
            	
            	}
            	
            	else
            	{
            MemberBean mb=new MemberBean();
            int userid=mb.getId(member);
		    ExpressageBean ex=new ExpressageBean();

			int expressageid=Integer.parseInt(request.getParameter("id"));
			//����
			
			
			
		    UserToExpressageBean ub=new UserToExpressageBean();
		    //����ǰ��鵱ǰԼ���Ƿ����,
		    int flag=ub.isExist(userid,expressageid);
		    if(flag==1)
		    {
		    	    request.setAttribute("message","The operation failed, the proxy information already exists.");
					request.getRequestDispatcher("./expressage").forward(request, response);
		    	    return ;
		    	
		    }
        //����Ƿ������Ѿ���ȡ        		    
		    flag=ex.check(expressageid);
		    if(flag==4)
		    {
		    	 request.setAttribute("message","The operation failed, it has been taken");
					request.getRequestDispatcher("./expressage").forward(request, response);
		    	    return ;
		    	
		    }
		    
		    	
    	    ub.insert(userid, expressageid);
			//�������õ�ǰΪ�Ѿ���ȡ
    	    
		    ex.deleteOne(expressageid);	
		    
		    request.setAttribute("message","If the information is successful, please go to the user center to view the details.");
			request.getRequestDispatcher("./expressage").forward(request, response);
		    }
			
		    
		}
		else if(action.equals("update2"))
		{
			//����ת������ҳ��׼������
			//����id����õ�ǰ��Ϣ
			int id=Integer.parseInt(request.getParameter("id"));
			ExpressageBean ex=new ExpressageBean();
		    ex=ex.getOneByUserId(id);
			
		    request.setAttribute("ex", ex);
			request.getRequestDispatcher("./member/prep/up2.jsp?id="+id).forward(request, response);
					
		}
		else if(action.equals("all"))
		{
		
				//��ѯ��ȫ������Ϣ������
				ExpressageBean ex=new ExpressageBean();
				ExpressageBean[] exlist=ex.getAllExpressage();
				
				
				
			    request.setAttribute("exlist", exlist);
				request.getRequestDispatcher("./admin/hzp/type2.jsp").forward(request, response);
			
		
			
					
		}
		else if(action.equals("all2"))
		{
			//���Ȩ��
			String username2 = (String)session.getAttribute("user");
			RoleBean roleBean=new RoleBean();
			int flag2=roleBean.selectRoleByUsername(username2);
			//���Ȩ��
			PermissionBean pb=new PermissionBean();
			int flag=pb.checkaffiche("expressage");
			if(flag==1||flag2==1)
			{
				//��ѯ��ȫ������Ϣ������
				ExpressageBean ex=new ExpressageBean();
				ExpressageBean[] exlist=ex.getAllExpressage();
				
				
				
			    request.setAttribute("exlist", exlist);
				request.getRequestDispatcher("./admin/hzp/type3.jsp").forward(request, response);
			
			}
			else
			{
				
				request.setAttribute("message", "Only super-supervisor permissions can be used, please confirm the permissions��");
				request.getRequestDispatcher("error2.jsp").forward(request, response);
				
			}
			
			
					
		}
		
		
		
		}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);

	}

}
