import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICashBook } from '../cash-book.model';
import { CashBookService } from '../service/cash-book.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './cash-book-delete-dialog.component.html',
})
export class CashBookDeleteDialogComponent {
  cashBook?: ICashBook;

  constructor(protected cashBookService: CashBookService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cashBookService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
