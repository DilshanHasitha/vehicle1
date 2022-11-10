import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EmployeeTypeFormService, EmployeeTypeFormGroup } from './employee-type-form.service';
import { IEmployeeType } from '../employee-type.model';
import { EmployeeTypeService } from '../service/employee-type.service';

@Component({
  selector: 'jhi-employee-type-update',
  templateUrl: './employee-type-update.component.html',
})
export class EmployeeTypeUpdateComponent implements OnInit {
  isSaving = false;
  employeeType: IEmployeeType | null = null;

  editForm: EmployeeTypeFormGroup = this.employeeTypeFormService.createEmployeeTypeFormGroup();

  constructor(
    protected employeeTypeService: EmployeeTypeService,
    protected employeeTypeFormService: EmployeeTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeType }) => {
      this.employeeType = employeeType;
      if (employeeType) {
        this.updateForm(employeeType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeType = this.employeeTypeFormService.getEmployeeType(this.editForm);
    if (employeeType.id !== null) {
      this.subscribeToSaveResponse(this.employeeTypeService.update(employeeType));
    } else {
      this.subscribeToSaveResponse(this.employeeTypeService.create(employeeType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(employeeType: IEmployeeType): void {
    this.employeeType = employeeType;
    this.employeeTypeFormService.resetForm(this.editForm, employeeType);
  }
}
