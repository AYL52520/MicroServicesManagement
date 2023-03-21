package yang.micro.exception;
public enum SDKExceptionEnums implements ExceptionEnums {
	OK("SDK0000000","成功"),
	ERROR("SDK9999999","SDK解析异常"),
	INITIALIZE_ERROR("SDK0000001","SDK初始化异常"),
	HTTPCONN_ERROR("SDK0000002","HTTP响应异常"),
	CHERSA_ERROR("SDK0000003","SDK验签失败"),
	INITIALIZE_KEYSTORE_ERROR("SDK0000004","初始化证书文件异常"),
	CHECK_FAIL("SDK0000005","字段校验异常"),
	APPROVEDEV_FAIL("SDK0000006","开发者认证失败"),
	SECURITY_ERROR("SDK0000007","SDK加签失败"),
	MerchantAuthSDK_ERROR("SDK0000008","认证移动端SDK失败"),
	POST_ERROR("SDK0000009","网络连接异常"),
	OAUTH_TOKEN_ERROR("SDK0000010","token认证失败"),
	SDK_UPLOAD_ERROR("SDK0000011","上传时出现异常");

    private String code;  
    private String message;  
      
    SDKExceptionEnums(String code, String message){
        this.code = code;  
        this.message = message;  
    }  
      
  
    @Override  
    public String getCode() {  
        return code;  
    }  
  
    @Override  
    public String getMessage() {  
        return message;  
    }


	@Override
	public void setMessage(String message) {
		this.message = message; 
	}  
    
    
  
}  