import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { UploadFileService } from 'src/app/services/upload-file.service';

@Component({
  selector: 'app-upload-files',
  templateUrl: './upload-files.component.html',
  styleUrls: ['./upload-files.component.css']
})
export class UploadFilesComponent implements OnInit {

  selectedFiles?: FileList;

  progressInfos: any[] = [];

  message: string[] = [];

  fileInfos?: Observable<any>;

  constructor(private uploadService: UploadFileService) { }

  ngOnInit(): void {

    this.fileInfos = this.uploadService.getFiles();

  }

  selectFiles(event: any): void {

    this.message = [];

    this.progressInfos = [];

    this.selectedFiles = event.target.files;

  }

  upload(idx: number, file: File) : void {

    this.progressInfos[idx] = { value: 0, fileName: file.name};

    if(file) {

      this.uploadService.upload(file).subscribe(
        (event: any) => {

          if(event.type === HttpEventType.UploadProgress) {

            this.progressInfos[idx].value = Math.round(100 * event.loaded / event.total);

          }else if(event instanceof HttpResponse) {

            const msg = 'Upload realizado com sucesso: ' + file.name;

            this.message.push(msg);

            this.fileInfos = this.uploadService.getFiles();

          }

        },
        (err: any) => {

          this.progressInfos[idx].value = 0;

          const msg = 'Não foi possível realizar o Upload do arquivo:' + file.name;

          this.message.push(msg);

          this.fileInfos = this.uploadService.getFiles();

        });

    }

  }

  uploadFiles(): void {

    this.message = [];

    if(this.selectedFiles) {

      for(let i = 0; i < this.selectedFiles.length; i++) {

        this.upload(i, this.selectedFiles[i]);

      }

    }

  }
}
