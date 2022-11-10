import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeeType } from '../employee-type.model';

@Component({
  selector: 'jhi-employee-type-detail',
  templateUrl: './employee-type-detail.component.html',
})
export class EmployeeTypeDetailComponent implements OnInit {
  employeeType: IEmployeeType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeType }) => {
      this.employeeType = employeeType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
