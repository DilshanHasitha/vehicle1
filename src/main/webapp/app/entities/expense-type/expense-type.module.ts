import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExpenseTypeComponent } from './list/expense-type.component';
import { ExpenseTypeDetailComponent } from './detail/expense-type-detail.component';
import { ExpenseTypeUpdateComponent } from './update/expense-type-update.component';
import { ExpenseTypeDeleteDialogComponent } from './delete/expense-type-delete-dialog.component';
import { ExpenseTypeRoutingModule } from './route/expense-type-routing.module';

@NgModule({
  imports: [SharedModule, ExpenseTypeRoutingModule],
  declarations: [ExpenseTypeComponent, ExpenseTypeDetailComponent, ExpenseTypeUpdateComponent, ExpenseTypeDeleteDialogComponent],
})
export class ExpenseTypeModule {}
