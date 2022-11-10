import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeeAccountBalance } from '../employee-account-balance.model';

@Component({
  selector: 'jhi-employee-account-balance-detail',
  templateUrl: './employee-account-balance-detail.component.html',
})
export class EmployeeAccountBalanceDetailComponent implements OnInit {
  employeeAccountBalance: IEmployeeAccountBalance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeAccountBalance }) => {
      this.employeeAccountBalance = employeeAccountBalance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
