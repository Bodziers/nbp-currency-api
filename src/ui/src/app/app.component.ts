import {Component, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Rate} from "./common/rate";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import { ApiService } from './services/api.service';
import { DateAdapter } from '@angular/material/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import * as _moment from 'moment';
import {MatDatepickerInputEvent} from "@angular/material/datepicker";
const moment = _moment;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'ui';
  rates: Rate[] = [];
  displayedColumns: string[] = ['currency', 'code', 'mid'];
  dataSource!: MatTableDataSource<Rate>;
  tableDateForm : FormGroup = this.fb.group({
    tblDate: ['']});
  tableDate = moment();
  selectedTblDate! : string;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private apiService: ApiService, private dateAdapter: DateAdapter<Date>, private fb :FormBuilder) {
  }

  ngOnInit() { }

  submitTableDate() {
      this.listRates(this.selectedTblDate)
      console.log(this.selectedTblDate)
  }

  listRates(date: string) : void {
    this.apiService.getRatesList(date).subscribe( {
      next : (result: Rate[]) => {
        this.dataSource = new MatTableDataSource<Rate>(result);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      } , error:() => {
        alert("Error getting exchange rates")
      }
    })
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  addDate(type: string, event: MatDatepickerInputEvent<Date>) {
    this.tableDate = moment(event.value);
    this.selectedTblDate = this.tableDate.format('YYYY-MM-DD' )
  }
}
