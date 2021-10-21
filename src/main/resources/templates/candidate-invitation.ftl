<!doctype html>
<html lang="en">
<head>
</head>
<body>
<div style="background: #ffffff;text-align: center; margin: 0; padding: 0px;">
	<table style="background:#fcfcfc;font-family:calibre,arial; max-width:650px;margin: 0 auto; padding-bottom: 0px; table-layout: fixed; width: 650px; ">
      	<tr>
      		<td style="text-align:center">
      			<img src="${serverUrl}/resources/images/feedback.png" style="max-width: 100%">
      		</td>
      	</tr>
      	<tr>
      		<td style="text-align:left;padding:0px 30px; ">
      			<div style="background:#fcfcfc; width:100%;  padding-bottom: 0px; float: left;">
			        <div style="text-align:left; padding:0px;">
			        	<p style="margin:0; padding:0; height:15px;">&nbsp;</p>
			          	<p style="color:#231f20; font-size:13px; font-weight:700; letter-spacing:0.3px; margin:0px; line-height:45px;">${staticContentsMap['candidate.suite.invitation.email.dear']} ${name},</p>
			          	<p style="font-size:13px; color:#231f20;margin:0; padding:0; line-height:20px;letter-spacing:0.3px; font-weight:400; text-align:justify;">
			          		${staticContentsMap['candidate.suite.invitation.email.content1']} 
				          	<b>zAspire Software Solutions</b> ${staticContentsMap['candidate.suite.invitation.email.content2']}  
				          	Aspire Liferay Developer ${staticContentsMap['candidate.suite.invitation.email.content3']}
			          	</p>
			          	<p style="margin:0; padding:0; height:15px;">&nbsp;</p>
				        <p style="font-size:13px; color:#231f20;margin:0; padding:0; line-height:20px;letter-spacing:0.3px; font-weight:400; text-align:justify;">
				          	${staticContentsMap['candidate.suite.invitation.email.content4']}
				        </p>
				        <p style="margin:0; padding:0; height:30px;">&nbsp;</p>
			        </div>
			        <div style="text-align: left; padding:0px 0px; background:#f2f2f2; margin:0px;">
			          <p style="margin:0px; padding:0; height:25px;">&nbsp;</p>
			          <p style="text-align:left; color:#231f20; font-size:15px; font-weight:400; letter-spacing:0.3px; margin:0px; line-height:20px;padding: 0 20px;">
			          	${staticContentsMap['candidate.suite.invitation.email.content5']}
			          	<a href="${serverUrl}${homeUrl}?token=${token}" target="_blank" style="color:#5443d5; font-weight: bold; text-decoration:none;">
			          		${serverUrl}
			          	</a> 
			          	${staticContentsMap['candidate.suite.invitation.email.content6']}
			          </p>
			          <p style="text-align:left; color:#231f20; font-size:13px; font-weight:400;letter-spacing:0.3px; margin:0px; line-height:20px; padding: 0 20px;">
			          	<span style="color:#343031; font-weight: 700;">${staticContentsMap['candidate.suite.invitation.email.content7']} </span> 
			          	<a href="mailto:${userEmail}" style="color:#5443d5; text-decoration:none;">${userEmail}</a>
			          </p>
			          <p style="height:25px;text-align: left;color: #231f20;font-size: 13px;font-weight: 400;letter-spacing: 0.3px;margin: 0px;line-height: 20px;padding: 0 20px;">${staticContentsMap['candidate.suite.invitation.email.content8']}</p>
			        </div>
			        <div>
			        	<p style="margin-bottom: 0;"><b>${staticContentsMap['candidate.suite.invitation.email.content9']}</b></p>
			        	<p style="margin-top: 0;">
			        		${staticContentsMap['candidate.suite.invitation.email.content10']}<br>
			        		${staticContentsMap['candidate.suite.invitation.email.content11']}<br>
			        		${staticContentsMap['candidate.suite.invitation.email.content12']}
			        	</p>
			        </div>
			        
			        <p style="letter-spacing:0.3px; font-size:12px; font-weight:400; text-align:left; color:#231f20; line-height:19px;margin: 0px ;">
			        	<span style="font-size:13;">${staticContentsMap['candidate.suite.invitation.email.sincerely']}</span><br>
			        	<span>${staticContentsMap['candidate.suite.invitation.email.content13']}</span>
			        </p>
			        <div style="clear: both;"></div>
			        <p style="margin:0px; padding:0; height:25px;">&nbsp;</p>
			        <p style="text-align:center; letter-spacing:0.2px; opacity:0.4; font-size:10px; color:#000; margin-bottom: 10px;">&copy; 2017 Kingsley Gate Partners</p>
			    </div>
			</td>
      	</tr>
    </table>
</div>
</body>
</html>
