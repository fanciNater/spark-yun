package com.isxcode.star.modules.file.controller;

import com.isxcode.star.api.file.pojos.req.DeleteFileReq;
import com.isxcode.star.api.file.pojos.req.DownloadFileReq;
import com.isxcode.star.api.file.pojos.req.PageFileReq;
import com.isxcode.star.api.file.pojos.res.PageFileRes;
import com.isxcode.star.api.main.constants.ModuleCode;
import com.isxcode.star.common.annotations.successResponse.SuccessResponse;
import com.isxcode.star.modules.file.service.FileBizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "资源文件中心模块")
@RestController
@RequestMapping(ModuleCode.FILE)
@RequiredArgsConstructor
public class FileController {

	private final FileBizService fileBizService;

	@Operation(summary = "资源文件上传接口")
	@PostMapping("/uploadFile")
	@SuccessResponse("上传成功")
	public void uploadFile(@RequestParam("file") @Schema(title = "文件") MultipartFile file,
			@RequestParam("type") @Schema(title = "文件类型") String type,
			@RequestParam("remark") @Schema(title = "备注") String remark) {

		fileBizService.uploadFile(file, type, remark);
	}

	@Operation(summary = "资源文件下载")
	@PostMapping("/downloadFile")
	@SuccessResponse("下载成功")
	public void downloadFile(@Valid @RequestBody DownloadFileReq downloadFileReq, HttpServletResponse response) {

		fileBizService.downloadFile(downloadFileReq, response);
	}

	@Operation(summary = "资源文件删除")
	@PostMapping("/deleteFile")
	@SuccessResponse("删除成功")
	public void deleteFile(@Valid @RequestBody DeleteFileReq deleteFileReq) {

		fileBizService.deleteFile(deleteFileReq);
	}

	@Operation(summary = "资源文件查询")
	@PostMapping("/pageFile")
	@SuccessResponse("查询成功")
	public Page<PageFileRes> pageFile(@Valid @RequestBody PageFileReq pageFileReq) {

		return fileBizService.pageFile(pageFileReq);
	}
}
