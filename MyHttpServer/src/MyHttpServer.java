import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class HTTPRequest {
    //declare attributions which will be the request client sent
    private String cmd_;   //command
    private String fileName_; //file name
    private String protocol_; //protocol
    private Map<String, String> headers_; //header


    HTTPRequest() {
        //constructor
        cmd_ = null;
        fileName_ = null;
        protocol_ = null;
        headers_ = new HashMap<String, String>();
    }

    //read the command as it in private
    public String getCmd_() {
        return cmd_;
    }

    //read the file name as it in private
    public String getFileName_() {
        return fileName_;
    }

    //read the protocol as it in private
    public String getProtocol_() {
        return protocol_;
    }

    //read the header as it in private
    public Map<String, String> getHeaders_() {
        return headers_;
    }


    public HTTPRequest getRequest (Socket inputSocket){
        HTTPRequest requestOutput = new HTTPRequest();
        InputStream inputStream = null;
            try {
                inputStream = inputSocket.getInputStream();
            } catch (IOException e) {
                System.out.print("Could not read the request");
            }

            Scanner sc = new Scanner(inputStream);

            requestOutput.cmd_ = sc.next();
            requestOutput.fileName_ = sc.next();
            requestOutput.protocol_ = sc.next();

            sc.nextLine();

            while(sc.hasNextLine()){
                String []tmp = sc.nextLine().split(": ",2);
                if(tmp[0].isEmpty()){
                    break;
                }
                requestOutput.headers_.put(tmp[0],tmp[1]);
                //System.out.println(tmp[0]+": "+tmp[1]);
        }
            return requestOutput;
    }
}



class HTTPResponse {

    private String status_;
    private String date_;
    private String contentType_;
    private String contentLength_;
    private ArrayList<String> fileContents_;

    HTTPResponse() {
        status_ = null;
        date_ = null;
        contentType_ = null;
        contentLength_ = null;
        fileContents_ = new ArrayList<>();
    }

    public String getStatus_() {
        return status_;
    }

    public String getDate_() {
        return date_;
    }

    public String getContentType_() {
        return contentType_;
    }

    public String getContentLength() {
        return contentLength_;
    }

    public ArrayList getFileContents() {
        return fileContents_;
    }

    public HTTPResponse giveResponse(HTTPRequest HttpRequestInput) {
        HTTPResponse response = new HTTPResponse();
        //OutputStream output = null;
        String fileName;
        if (HttpRequestInput.getFileName_().equals("/")) {
            fileName = "/Users/angzhang/Desktop/Fall2021/azhang/CS6011/Week1/Day4/resource/index.html";
        } else {
            fileName = "/Users/angzhang/Desktop/Fall2021/azhang/CS6011/Week1/Day4/resource" + HttpRequestInput.getFileName_();
        }

        File out = new File(fileName);

        if (!out.exists()) {
            response.status_ = HttpRequestInput.getProtocol_() + " 404 Not Found.";
            response.date_ = "Date: " + java.time.LocalDate.now();
            response.contentType_ = "Content-Type: text/html";
            response.contentLength_ = "Content-Length:" + out.length();
            response.fileContents_.add("<b><h1> Page Not Found</h1></b>");
            response.fileContents_.add("The request was not found on this server.");
        } else {
            Scanner scOut = null;
            try {
                scOut = new Scanner(out);
            } catch (FileNotFoundException e) {
                System.out.print("File not exist");
                System.exit(-1);
            }

            response.status_ = HttpRequestInput.getProtocol_() + " 200 ok";
            response.date_ = "Date: " + java.time.LocalDate.now();
            response.contentType_ = "Content-Type:text/html";
            response.contentLength_ = "Content-Length:" + out.length();

            while (scOut.hasNextLine()) {
                response.fileContents_.add(scOut.nextLine());
            }

        }

        return response;
    }
}


public class MyHttpServer {
    public static void main(String[] args) throws IOException {
        int portNumber = 8080;
        ServerSocket serverSocket = null;
        try {
            //set an exception that if the port number is invalid
            //like out of range or has been used
            //then it will return a message and close the process
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.print("Invalid port number" + portNumber);
            System.exit(-1);
        }

        //handle data from client
        while (true) {
            Socket client = null;
            try {
                client = serverSocket.accept();
            } catch (IOException e) {
                System.out.print("Accept failed");
                System.exit(-1);
            }

            HTTPRequest testRequest = new HTTPRequest();
            testRequest = testRequest.getRequest(client);

            InputStream input = null;
            try {
                input = client.getInputStream();
            } catch (IOException e) {
                System.out.print("Could not read the request");
            }

            Scanner sc = new Scanner(input);

            System.out.println(testRequest.getCmd_());
            System.out.println(testRequest.getFileName_());
            System.out.println(testRequest.getProtocol_());

            HTTPResponse testResponse = new HTTPResponse();
            testResponse = testResponse.giveResponse(testRequest);

            //OutputStream output =null;
            OutputStream output = client.getOutputStream();
            PrintWriter pw = new PrintWriter(output);

            System.out.println(testResponse.getStatus_());
            System.out.println(testResponse.getDate_());
            System.out.println(testResponse.getContentType_());
            System.out.println(testResponse.getContentLength());


            pw.println(testResponse.getStatus_());
           pw.println(testResponse.getContentLength());
            pw.println();

            for (int i = 0; i < testResponse.getFileContents().size(); i++) {
                pw.println(testResponse.getFileContents().get(i));
            }


            //NEED TO PUT A BLANK LINE
            pw.println();
            pw.flush();
            try {
                client.close();
            } catch (IOException e) {
                System.out.print("could not close the socket.");
            }
            pw.close();
        }

    }
}


