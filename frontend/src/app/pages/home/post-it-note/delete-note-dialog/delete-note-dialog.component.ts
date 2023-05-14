import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-delete-note-dialog',
  templateUrl: './delete-note-dialog.component.html',
  styleUrls: ['./delete-note-dialog.component.css'],
})
export class DeleteNoteDialogComponent implements OnInit {
  message: string = 'Do you want to delete this note?';
  confirmText: string = 'Yes';
  cancelText: string = 'No';

  constructor(public dialogRef: MatDialogRef<DeleteNoteDialogComponent>) {}

  ngOnInit(): void {}
}
