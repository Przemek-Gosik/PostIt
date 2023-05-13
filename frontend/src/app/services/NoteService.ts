import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Note} from "../models/Note";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class NoteService{

  private apiUrl="http://localhost:8080/api/note";

  constructor(
    private http: HttpClient
  ) {}

  createNote(note:Note):Observable<Note>{
    return this.http.post<Note>(`${this.apiUrl}`,note);
  }

  editNote(note:Note):Observable<Note>{
    return this.http.put<Note>(`${this.apiUrl}`,note);
  }

  getAllNotes():Observable<Note[]>{
    return this.http.get<Note[]>(`${this.apiUrl}`);
  }

  deleteNote(id: number){
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
