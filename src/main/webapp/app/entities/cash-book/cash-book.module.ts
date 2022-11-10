import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CashBookComponent } from './list/cash-book.component';
import { CashBookDetailComponent } from './detail/cash-book-detail.component';
import { CashBookUpdateComponent } from './update/cash-book-update.component';
import { CashBookDeleteDialogComponent } from './delete/cash-book-delete-dialog.component';
import { CashBookRoutingModule } from './route/cash-book-routing.module';

@NgModule({
  imports: [SharedModule, CashBookRoutingModule],
  declarations: [CashBookComponent, CashBookDetailComponent, CashBookUpdateComponent, CashBookDeleteDialogComponent],
})
export class CashBookModule {}
