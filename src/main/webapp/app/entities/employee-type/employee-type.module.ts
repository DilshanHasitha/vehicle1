import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeTypeComponent } from './list/employee-type.component';
import { EmployeeTypeDetailComponent } from './detail/employee-type-detail.component';
import { EmployeeTypeUpdateComponent } from './update/employee-type-update.component';
import { EmployeeTypeDeleteDialogComponent } from './delete/employee-type-delete-dialog.component';
import { EmployeeTypeRoutingModule } from './route/employee-type-routing.module';

@NgModule({
  imports: [SharedModule, EmployeeTypeRoutingModule],
  declarations: [EmployeeTypeComponent, EmployeeTypeDetailComponent, EmployeeTypeUpdateComponent, EmployeeTypeDeleteDialogComponent],
})
export class EmployeeTypeModule {}
