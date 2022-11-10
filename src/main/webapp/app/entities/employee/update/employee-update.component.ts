import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeeFormService, EmployeeFormGroup } from './employee-form.service';
import { IEmployee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmployeeType } from 'app/entities/employee-type/employee-type.model';
import { EmployeeTypeService } from 'app/entities/employee-type/service/employee-type.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  employee: IEmployee | null = null;

  usersSharedCollection: IUser[] = [];
  employeeTypesSharedCollection: IEmployeeType[] = [];

  editForm: EmployeeFormGroup = this.employeeFormService.createEmployeeFormGroup();

  constructor(
    protected employeeService: EmployeeService,
    protected employeeFormService: EmployeeFormService,
    protected userService: UserService,
    protected employeeTypeService: EmployeeTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareEmployeeType = (o1: IEmployeeType | null, o2: IEmployeeType | null): boolean =>
    this.employeeTypeService.compareEmployeeType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.employee = employee;
      if (employee) {
        this.updateForm(employee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.employeeFormService.getEmployee(this.editForm);
    if (employee.id !== null) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.employee = employee;
    this.employeeFormService.resetForm(this.editForm, employee);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, employee.user);
    this.employeeTypesSharedCollection = this.employeeTypeService.addEmployeeTypeToCollectionIfMissing<IEmployeeType>(
      this.employeeTypesSharedCollection,
      employee.type
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.employee?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.employeeTypeService
      .query()
      .pipe(map((res: HttpResponse<IEmployeeType[]>) => res.body ?? []))
      .pipe(
        map((employeeTypes: IEmployeeType[]) =>
          this.employeeTypeService.addEmployeeTypeToCollectionIfMissing<IEmployeeType>(employeeTypes, this.employee?.type)
        )
      )
      .subscribe((employeeTypes: IEmployeeType[]) => (this.employeeTypesSharedCollection = employeeTypes));
  }
}
