package in.bluebytes.superbox.controller;

import in.bluebytes.superbox.model.User;
import in.bluebytes.superbox.model.UserFile;
import in.bluebytes.superbox.service.UserFileService;
import in.bluebytes.superbox.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final UserFileService userFileService;
    private final UserService userService;

    @PostMapping("/uploadFile")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile multipartFile, Model model) {

        String fileUploadError = null;

        //Get logged in user from persistence
        User user = userService.getUser(authentication.getName());

        //Check if the file is empty or user uploaded an empty file
        if(multipartFile.isEmpty()) {
            fileUploadError = "Please select a file to upload or the file to be uploaded has no content.";
        }

        //Clean the path and get the file name
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        log.info(String.format("Uploaded file name: %s", fileName));

        //File name should be unique for an user
        if(!userFileService.isFileAvailable(fileName, user.getUserId())) {
            fileUploadError = "A file with the same file name already exists. Try uploading with a " +
                    "different name.";
        }

        //Prepare the file to upload
        UserFile userFile = userFileService.createUserFile(multipartFile, user);

        if(null != userFile && null == fileUploadError) {
            int rowsAdded = userFileService.insertFile(userFile);
            if(rowsAdded < 0) {
                fileUploadError = "There was an error in persisting the file. Please try after some time.";
            }
        } else if(null == fileUploadError) {
            fileUploadError = "There was an error in uploading the file. Try after some time.";
        }

        if(null == fileUploadError) {
            model.addAttribute("modalMessage", "File has been added successfully.");
            model.addAttribute("modalMsgTitle", "Success");
        } else {
            model.addAttribute("modalMessage", fileUploadError);
            model.addAttribute("modalMsgTitle", "Failure");
        }

        return "home";
    }

    @GetMapping("/downloadFile/{fileName}")
    public ResponseEntity<Resource> downloadFile(Authentication authentication, @PathVariable String fileName) {

        //Get logged in user from persistence
        User user = userService.getUser(authentication.getName());

        UserFile userFile = userFileService.getFileByName(fileName, user.getUserId());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(userFile.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userFile.getFileName() + "\"")
                .body(new ByteArrayResource(userFile.getFileData()));
    }

    @GetMapping("/deleteFile/{fileName}")
    public String deleteFile(Authentication authentication, @PathVariable String fileName, Model model) {

        //Get logged in user from persistence
        User user = userService.getUser(authentication.getName());

        UserFile userFile = userFileService.getFileByName(fileName, user.getUserId());

        int rowsDeleted = userFileService.deleteFile(userFile.getFileId());

        if(rowsDeleted < 0) {
            model.addAttribute("modalMessage", "Unable to delete the selected file. Try after some time.");
            model.addAttribute("modalMsgTitle", "File Upload Failure");
        } else {
            model.addAttribute("modalMessage", "Selected file has been deleted successfully.");
            model.addAttribute("modalMsgTitle", "File Upload Success");
        }

        return "home";
    }

}
