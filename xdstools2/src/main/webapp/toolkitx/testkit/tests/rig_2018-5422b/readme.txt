Multiple Transfer Syntaxes, Single IDS
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="content-type" content="text/html;
      charset=windows-1252">
    <title>Responding Imaging Gateway: Multiple Transfer Syntaxes,
      Single IDS</title>
  </head>
  <body>
    <h2>Multiple Transfer Syntaxes, Single IDS</h2>
    <p>Tests the ability of the Responding Imaging Gateway actor (SUT)
      to respond correctly to a Cross Gateway Retrieve Imaging Document
      Set (RAD-75) transaction from an Initiating Imaging Gateway actor
      (Simulator), for a single DICOM image file with two acceptable
      Transfer Syntaxes. </p>
    <p>The image is not available on the Imaging Document Source with
      the first transfer syntax but is available with the second. </p>
    <h3>Retrieve Parameters</h3>
    <table border="1">
      <tbody>
        <tr>
          <td>RIG Home Community ID</td>
          <td>urn:oid:1.3.6.1.4.1.21367.13.70.201</td>
        </tr>
        <tr>
          <td>IDS Repository Unique ID (E)</td>
          <td>1.3.6.1.4.1.21367.13.71.201.1</td>
        </tr>
        <tr>
          <td>Transfer Syntax UIDs</td>
          <td>
            <ul>
              <li>1.2.840.10008.1.2.4.50: JPEG Baseline (Process 1) </li>
              <li>1.2.840.10008.1.2.4.70: JPEG lossless</li>
            </ul>
          </td>
        </tr>
      </tbody>
    </table>
    <h3>Test Execution</h3>
    <p>The test consists of four steps: </p>
    <ol>
      <li>Test software sends RAD-75 request to System Under test and
        records response. The request contains requests for images in
        two separate DICOM studies.<br>
      </li>
      <ul>
        <li>System Under Test sends a RAD-69 request to Imaging Document
          Source which stores the request.</li>
        <li>Imaging Document Source provides RAD-69 response to System
          Under Test.</li>
        <li>System Under Test provides RAD-75 response to test software.<br>
        </li>
      </ul>
      <li>Test software validates the RAD-69 request that is sent by the
        System Under Test.</li>
      <li>Test software validates the RAD-75 response sent by the System
        Under Test.</li>
      <li>Test software validates the image returned in the RAD-75
        response to make sure the System Under Test did not alter the
        image.<br>
      </li>
    </ol>
  </body>
</html>
