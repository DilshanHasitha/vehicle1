import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeAccountComponent } from './list/employee-account.component';
import { EmployeeAccountDetailComponent } from './detail/employee-account-detail.component';
import { EmployeeAccountUpdateComponent } from './update/employee-account-update.component';
import { EmployeeAccountDeleteDialogComponent } from './delete/employee-account-delete-dialog.component';
import { EmployeeAccountRoutingModule } from './route/employee-account-routing.module';

@NgModule({
  imports: [SharedModule, EmployeeAccountRoutingModule],
  declarations: [
    EmployeeAccountComponent,
    EmployeeAccountDetailComponent,
    EmployeeAccountUpdateComponent,
    EmployeeAccountDeleteDialogComponent,
  ],
})
export class EmployeeAccountModule {}
