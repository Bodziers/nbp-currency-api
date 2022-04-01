import {Component, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Rate} from "./common/rate";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import { ApiService } from './services/api.service';
import { DateAdapter } from '@angular/material/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
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
  currencies: string[] = ['PLN',	'EUR',	'GBP',	'USD',	'CHF',	'CZK',	'DKK',
    'AUD',	'BGN',	'BRL',	'CAD',	'CLP',	'CNY',	'HKD',	'HRK',	'HUF',	'IDR',
    'ILS',	'INR',	'ISK',	'JPY',	'KRW',	'MXN',	'MYR',	'NOK',	'NZD',	'PHP',
    'RON',	'SEK',	'SGD',	'THB',	'TRY',	'UAH',	'XDR',	'ZAR'];

  displayedColumns: string[] = ['currency', 'code', 'mid'];
  dataSource!: MatTableDataSource<Rate>;

  tableDate = moment();
  calcDate = moment();
  selectedCalcDate! :string;
  selectedTblDate! : string;
  calcAmount:string = '';
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  tableDateForm : FormGroup = this.fb.group({
    tblDate: ['', Validators.required]
  });

  calcDateForm : FormGroup = this.fb.group({
    amount: ['',Validators.required],
    calcDate: ['',Validators.required],
    fromCurrency: ['', Validators.required],
    toCurrency: ['', Validators.required]
  });


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

  saveTableDate() : void {
    this.apiService.saveRatesToFile(this.selectedTblDate).subscribe()
    this.listRates(this.selectedTblDate)
  }

  exchangeAmount()  {
    return this.apiService.calculateExRate(
      this.selectedCalcDate,
      this.calcDateForm.get('fromCurrency')?.value,
      this.calcDateForm.get('toCurrency')?.value,
      this.calcDateForm.get('amount')?.value)
      .subscribe({
        next: result => {
          this.calcAmount = result.toString()
          console.log(this.calcDateForm.value)
        }, error: () => {
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
  // This method allows to bind picked date in table field with variable
  addDate(type: string, event: MatDatepickerInputEvent<Date>) {
    this.tableDate = moment(event.value);
    this.selectedTblDate = this.tableDate.format('YYYY-MM-DD' )
  }

  // This method allows to bind picked date in calculator field with variable
  addCalcDate(input: string, $event: MatDatepickerInputEvent<Date>) {
    this.calcDate = moment($event.value);
    this.selectedCalcDate = this.calcDate.format('YYYY-MM-DD');
  }


}
