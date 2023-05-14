import { Component, OnInit } from '@angular/core';
import { NoteService } from '../../services/NoteService';
import { Note } from '../../models/Note';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  notes: Note[] = [];

  constructor(private noteService: NoteService) {}

  ngOnInit(): void {
    this.getNotes();
  }

  getNotes(): void {
    this.noteService.getAllNotes().subscribe((res: Note[]) => {
      console.log(res);
      this.notes = res;
    });
  }

  addNewNote(): void {
    this.notes.push({
      text: '',
    });
  }

  deleteNote(index: number): void {
    this.notes.splice(index, 1);
  }
}
